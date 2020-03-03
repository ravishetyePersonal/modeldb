package ai.verta.modeldb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ai.verta.modeldb.utils.TestConstants.RESOURCE_OWNER_ID;

import ai.verta.modeldb.authservice.AuthService;
import ai.verta.modeldb.authservice.AuthServiceUtils;
import ai.verta.modeldb.authservice.PublicAuthServiceUtils;
import ai.verta.modeldb.authservice.PublicRoleServiceUtils;
import ai.verta.modeldb.authservice.RoleService;
import ai.verta.modeldb.authservice.RoleServiceUtils;
import ai.verta.modeldb.utils.ModelDBUtils;
import ai.verta.modeldb.versioning.Blob;
import ai.verta.modeldb.versioning.BlobExpanded;
import ai.verta.modeldb.versioning.Commit;
import ai.verta.modeldb.versioning.CreateCommitRequest;
import ai.verta.modeldb.versioning.DatasetBlob;
import ai.verta.modeldb.versioning.DeleteCommitRequest;
import ai.verta.modeldb.versioning.DeleteRepositoryRequest;
import ai.verta.modeldb.versioning.DeleteTagRequest;
import ai.verta.modeldb.versioning.GetCommitComponentRequest;
import ai.verta.modeldb.versioning.GetRepositoryRequest;
import ai.verta.modeldb.versioning.GetTagRequest;
import ai.verta.modeldb.versioning.ListCommitBlobsRequest;
import ai.verta.modeldb.versioning.ListTagsRequest;
import ai.verta.modeldb.versioning.PathDatasetBlob;
import ai.verta.modeldb.versioning.PathDatasetComponentBlob;
import ai.verta.modeldb.versioning.Repository;
import ai.verta.modeldb.versioning.RepositoryIdentification;
import ai.verta.modeldb.versioning.RepositoryNamedIdentification;
import ai.verta.modeldb.versioning.SetRepository;
import ai.verta.modeldb.versioning.SetRepository.Response;
import ai.verta.modeldb.versioning.SetTagRequest;
import ai.verta.modeldb.versioning.VersioningServiceGrpc;
import ai.verta.modeldb.versioning.VersioningServiceGrpc.VersioningServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status.Code;
import io.grpc.StatusRuntimeException;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RepositoryTest {

  private static final Logger LOGGER = LogManager.getLogger(RepositoryTest.class);
  private static final String NAME = "repository_name";
  private static final String NAME_2 = "repository_name2";
  /**
   * This rule manages automatic graceful shutdown for the registered servers and channels at the
   * end of test.
   */
  @Rule public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

  private ManagedChannel channel = null;
  private ManagedChannel client2Channel = null;
  private ManagedChannel authServiceChannel = null;
  private static String serverName = InProcessServerBuilder.generateName();
  private static InProcessServerBuilder serverBuilder =
      InProcessServerBuilder.forName(serverName).directExecutor();
  private static InProcessChannelBuilder channelBuilder =
      InProcessChannelBuilder.forName(serverName).directExecutor();
  private static InProcessChannelBuilder client2ChannelBuilder =
      InProcessChannelBuilder.forName(serverName).directExecutor();
  private static AuthClientInterceptor authClientInterceptor;
  private static App app;

  @SuppressWarnings("unchecked")
  @BeforeClass
  public static void setServerAndService() throws Exception {

    Map<String, Object> propertiesMap =
        ModelDBUtils.readYamlProperties(System.getenv(ModelDBConstants.VERTA_MODELDB_CONFIG));
    Map<String, Object> testPropMap = (Map<String, Object>) propertiesMap.get("test");
    Map<String, Object> databasePropMap = (Map<String, Object>) testPropMap.get("test-database");

    app = App.getInstance();
    AuthService authService = new PublicAuthServiceUtils();
    RoleService roleService = new PublicRoleServiceUtils(authService);

    Map<String, Object> authServicePropMap =
        (Map<String, Object>) propertiesMap.get(ModelDBConstants.AUTH_SERVICE);
    if (authServicePropMap != null) {
      String authServiceHost = (String) authServicePropMap.get(ModelDBConstants.HOST);
      Integer authServicePort = (Integer) authServicePropMap.get(ModelDBConstants.PORT);
      app.setAuthServerHost(authServiceHost);
      app.setAuthServerPort(authServicePort);

      authService = new AuthServiceUtils();
      roleService = new RoleServiceUtils(authService);
    }

    App.initializeServicesBaseOnDataBase(
        serverBuilder, databasePropMap, propertiesMap, authService, roleService);
    serverBuilder.intercept(new ModelDBAuthInterceptor());

    Map<String, Object> testUerPropMap = (Map<String, Object>) testPropMap.get("testUsers");
    if (testUerPropMap != null && testUerPropMap.size() > 0) {
      authClientInterceptor = new AuthClientInterceptor(testPropMap);
      channelBuilder.intercept(authClientInterceptor.getClient1AuthInterceptor());
      client2ChannelBuilder.intercept(authClientInterceptor.getClient2AuthInterceptor());
    }
  }

  @AfterClass
  public static void removeServerAndService() {
    App.initiateShutdown(0);
  }

  @After
  public void clientClose() {
    if (!channel.isShutdown()) {
      channel.shutdownNow();
    }
    if (!client2Channel.isShutdown()) {
      client2Channel.shutdownNow();
    }

    if (app.getAuthServerHost() != null && app.getAuthServerPort() != null) {
      if (!authServiceChannel.isShutdown()) {
        authServiceChannel.shutdownNow();
      }
    }
  }

  @Before
  public void initializeChannel() throws IOException {
    grpcCleanup.register(serverBuilder.build().start());
    channel = grpcCleanup.register(channelBuilder.maxInboundMessageSize(1024).build());
    client2Channel =
        grpcCleanup.register(client2ChannelBuilder.maxInboundMessageSize(1024).build());
    if (app.getAuthServerHost() != null && app.getAuthServerPort() != null) {
      authServiceChannel =
          ManagedChannelBuilder.forTarget(app.getAuthServerHost() + ":" + app.getAuthServerPort())
              .usePlaintext()
              .intercept(authClientInterceptor.getClient1AuthInterceptor())
              .build();
    }
  }

  @Test
  public void repositoryTest() {
    LOGGER.info("Create and delete repository test start................................");

    VersioningServiceBlockingStub versioningServiceBlockingStub =
        VersioningServiceGrpc.newBlockingStub(channel);

    SetRepository setRepository =
        SetRepository.newBuilder()
            .setId(
                RepositoryIdentification.newBuilder()
                    .setNamedId(RepositoryNamedIdentification.newBuilder().setName(NAME).build())
                    .build())
            .setRepository(Repository.newBuilder().setName(NAME))
            .build();
    Response result = versioningServiceBlockingStub.createRepository(setRepository);
    long id = result.getRepository().getId();
    try {
      setRepository =
          SetRepository.newBuilder()
              .setId(
                  RepositoryIdentification.newBuilder()
                      .setNamedId(RepositoryNamedIdentification.newBuilder()
                          .setWorkspaceName("test1verta_gmail_com").setName(NAME_2).build())
                      .build())
              .setRepository(Repository.newBuilder().setName(NAME))
              .build();
      versioningServiceBlockingStub.createRepository(setRepository);
      Assert.fail();
    } catch (StatusRuntimeException e) {
      Assert.assertEquals(Code.PERMISSION_DENIED, e.getStatus().getCode());
      e.printStackTrace();
    }

    // check id
    GetRepositoryRequest getRepositoryRequest =
        GetRepositoryRequest.newBuilder()
            .setId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .build();
    GetRepositoryRequest.Response getByIdResult =
        versioningServiceBlockingStub.getRepository(getRepositoryRequest);
    Assert.assertTrue(getByIdResult.hasRepository());

    setRepository =
        SetRepository.newBuilder()
            .setId(
                RepositoryIdentification.newBuilder()
                    .setNamedId(RepositoryNamedIdentification.newBuilder().setName(NAME).build())
                    .build())
            .setRepository(Repository.newBuilder().setName(NAME_2))
            .build();
    result = versioningServiceBlockingStub.updateRepository(setRepository);
    Assert.assertTrue(result.hasRepository());
    Assert.assertEquals(NAME_2, result.getRepository().getName());

    try {
      // check name
      getRepositoryRequest =
          GetRepositoryRequest.newBuilder()
              .setId(
                  RepositoryIdentification.newBuilder()
                      .setNamedId(RepositoryNamedIdentification.newBuilder().setName(NAME)))
              .build();
      versioningServiceBlockingStub.getRepository(getRepositoryRequest);
      Assert.fail();
    } catch (StatusRuntimeException e) {
      Assert.assertEquals(Code.NOT_FOUND, e.getStatus().getCode());
      e.printStackTrace();
    }

    getRepositoryRequest =
        GetRepositoryRequest.newBuilder()
            .setId(
                RepositoryIdentification.newBuilder()
                    .setNamedId(RepositoryNamedIdentification.newBuilder().setName(NAME_2)))
            .build();
    GetRepositoryRequest.Response getByNameResult =
        versioningServiceBlockingStub.getRepository(getRepositoryRequest);
    Assert.assertTrue(getByNameResult.hasRepository());
    Assert.assertEquals(RESOURCE_OWNER_ID, getByNameResult.getRepository().getOwner());

    DeleteRepositoryRequest deleteRepository =
        DeleteRepositoryRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id))
            .build();
    DeleteRepositoryRequest.Response deleteResult =
        versioningServiceBlockingStub.deleteRepository(deleteRepository);
    Assert.assertTrue(deleteResult.getStatus());

    LOGGER.info("Create and delete repository test end................................");
  }

  @Test
  public void addTagTest() {
    LOGGER.info("Add tags test start................................");

    VersioningServiceBlockingStub versioningServiceBlockingStub =
        VersioningServiceGrpc.newBlockingStub(channel);

    SetRepository setRepository =
        SetRepository.newBuilder()
            .setId(
                RepositoryIdentification.newBuilder()
                    .setNamedId(
                        RepositoryNamedIdentification.newBuilder().setName("Repo-1").build())
                    .build())
            .setRepository(Repository.newBuilder().setName("Repo-1"))
            .build();
    SetRepository.Response result = versioningServiceBlockingStub.createRepository(setRepository);
    long id = result.getRepository().getId();

    CreateCommitRequest createCommitRequest =
        CreateCommitRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .setCommit(
                Commit.newBuilder()
                    .setAuthor(authClientInterceptor.getClient1Email())
                    .setMessage("this is the test commit message")
                    .setDateCreated(Calendar.getInstance().getTimeInMillis())
                    .build())
            .addBlobs(
                BlobExpanded.newBuilder()
                    .setBlob(
                        Blob.newBuilder()
                            .setDataset(
                                DatasetBlob.newBuilder()
                                    .setPath(
                                        PathDatasetBlob.newBuilder()
                                            .addComponents(
                                                PathDatasetComponentBlob.newBuilder()
                                                    .setPath("/public/versioning.proto")
                                                    .setSize(2)
                                                    .setLastModifiedAtSource(
                                                        Calendar.getInstance().getTimeInMillis())
                                                    .build())
                                            .build())
                                    .build())
                            .build())
                    .addLocation("public")
                    .build())
            .build();

    CreateCommitRequest.Response commitResponse =
        versioningServiceBlockingStub.createCommit(createCommitRequest);

    String tagName = "backend-commit-tag-1";
    SetTagRequest setTagRequest =
        SetTagRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .setCommitSha(commitResponse.getCommit().getCommitSha())
            .setTag(tagName)
            .build();

    versioningServiceBlockingStub.setTag(setTagRequest);

    GetTagRequest getTagRequest =
        GetTagRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .setTag(tagName)
            .build();
    GetTagRequest.Response getTagResponse = versioningServiceBlockingStub.getTag(getTagRequest);

    assertEquals(
        "Expected tag not found in response",
        commitResponse.getCommit(),
        getTagResponse.getCommit());

    ListTagsRequest listTagsRequest =
        ListTagsRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .build();
    ListTagsRequest.Response listTagsResponse =
        versioningServiceBlockingStub.listTags(listTagsRequest);
    assertEquals(
        "Tag count not match with expected tag count", 1, listTagsResponse.getTotalRecords());
    assertTrue(
        "Expected tag not found in the response", listTagsResponse.getTagsList().contains(tagName));

    setTagRequest =
        SetTagRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .setCommitSha(commitResponse.getCommit().getCommitSha())
            .setTag(tagName)
            .build();

    try {
      versioningServiceBlockingStub.setTag(setTagRequest);
      Assert.fail();
    } catch (StatusRuntimeException e) {
      Assert.assertEquals(Code.NOT_FOUND, e.getStatus().getCode());
    }

    DeleteTagRequest deleteTagRequest =
        DeleteTagRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .setTag(tagName)
            .build();
    versioningServiceBlockingStub.deleteTag(deleteTagRequest);

    deleteTagRequest =
        DeleteTagRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .setTag(tagName)
            .build();
    try {
      versioningServiceBlockingStub.deleteTag(deleteTagRequest);
      Assert.fail();
    } catch (StatusRuntimeException e) {
      Assert.assertEquals(Code.NOT_FOUND, e.getStatus().getCode());
    }

    listTagsRequest =
        ListTagsRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .build();
    listTagsResponse = versioningServiceBlockingStub.listTags(listTagsRequest);
    assertEquals(
        "Tag count not match with expected tag count", 0, listTagsResponse.getTotalRecords());

    DeleteRepositoryRequest deleteRepository =
        DeleteRepositoryRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id))
            .build();
    DeleteRepositoryRequest.Response deleteResult =
        versioningServiceBlockingStub.deleteRepository(deleteRepository);
    Assert.assertTrue(deleteResult.getStatus());

    LOGGER.info("Add tag test end................................");
  }

  @Test
  public void getCommitBlobTest() {
    LOGGER.info("Get commit blob test start................................");

    VersioningServiceBlockingStub versioningServiceBlockingStub =
        VersioningServiceGrpc.newBlockingStub(channel);

    SetRepository setRepository =
        SetRepository.newBuilder()
            .setId(
                RepositoryIdentification.newBuilder()
                    .setNamedId(
                        RepositoryNamedIdentification.newBuilder().setName("Repo-1").build())
                    .build())
            .setRepository(Repository.newBuilder().setName("Repo-1"))
            .build();
    SetRepository.Response result = versioningServiceBlockingStub.createRepository(setRepository);
    long id = result.getRepository().getId();

    String path = "/protos/proto/public/versioning/versioning.proto";
    List<String> location = new ArrayList<>();
    location.add("modeldb");
    location.add("environment");
    location.add("train");
    Blob blob =
        Blob.newBuilder()
            .setDataset(
                DatasetBlob.newBuilder()
                    .setPath(
                        PathDatasetBlob.newBuilder()
                            .addComponents(
                                PathDatasetComponentBlob.newBuilder()
                                    .setPath(path)
                                    .setSize(2)
                                    .setLastModifiedAtSource(
                                        Calendar.getInstance().getTimeInMillis())
                                    .build())
                            .build())
                    .build())
            .build();
    CreateCommitRequest createCommitRequest =
        CreateCommitRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .setCommit(
                Commit.newBuilder()
                    .setAuthor(authClientInterceptor.getClient1Email())
                    .setMessage("this is the test commit message")
                    .setDateCreated(Calendar.getInstance().getTimeInMillis())
                    .build())
            .addBlobs(BlobExpanded.newBuilder().setBlob(blob).addAllLocation(location).build())
            .build();

    CreateCommitRequest.Response commitResponse =
        versioningServiceBlockingStub.createCommit(createCommitRequest);

    GetCommitComponentRequest getCommitBlobRequest =
        GetCommitComponentRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .setCommitSha(commitResponse.getCommit().getCommitSha())
            .addAllLocation(location)
            .build();
    GetCommitComponentRequest.Response getCommitBlobResponse =
        versioningServiceBlockingStub.getCommitComponent(getCommitBlobRequest);
    assertEquals(
        "Blob path not match with expected blob path",
        path,
        getCommitBlobResponse.getBlob().getDataset().getPath().getComponents(0).getPath());

    location.add("xyz");
    getCommitBlobRequest =
        GetCommitComponentRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .setCommitSha(commitResponse.getCommit().getCommitSha())
            .addAllLocation(location)
            .build();
    try {
      versioningServiceBlockingStub.getCommitComponent(getCommitBlobRequest);
      Assert.fail();
    } catch (StatusRuntimeException e) {
      Assert.assertEquals(Code.NOT_FOUND, e.getStatus().getCode());
      e.printStackTrace();
    }

    // TODO: Add Delete Commit code here

    DeleteRepositoryRequest deleteRepository =
        DeleteRepositoryRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id))
            .build();
    DeleteRepositoryRequest.Response deleteResult =
        versioningServiceBlockingStub.deleteRepository(deleteRepository);
    Assert.assertTrue(deleteResult.getStatus());

    LOGGER.info("Get commit blob test end................................");
  }

  private Blob getBlob(String path) {
    return Blob.newBuilder()
        .setDataset(
            DatasetBlob.newBuilder()
                .setPath(
                    PathDatasetBlob.newBuilder()
                        .addComponents(
                            PathDatasetComponentBlob.newBuilder()
                                .setPath(path)
                                .setSize(2)
                                .setLastModifiedAtSource(Calendar.getInstance().getTimeInMillis())
                                .build())
                        .build())
                .build())
        .build();
  }

  @Test
  public void getCommitBlobListTest() {
    LOGGER.info("List commit blob test start................................");

    VersioningServiceBlockingStub versioningServiceBlockingStub =
        VersioningServiceGrpc.newBlockingStub(channel);

    SetRepository setRepository =
        SetRepository.newBuilder()
            .setId(
                RepositoryIdentification.newBuilder()
                    .setNamedId(
                        RepositoryNamedIdentification.newBuilder().setName("Repo-1").build())
                    .build())
            .setRepository(Repository.newBuilder().setName("Repo-1"))
            .build();
    SetRepository.Response result = versioningServiceBlockingStub.createRepository(setRepository);
    long id = result.getRepository().getId();

    String path1 = "/protos/proto/public/versioning/versioning.proto";
    List<String> location1 = new ArrayList<>();
    location1.add("modeldb");
    location1.add("environment");
    location1.add("train.json"); // file
    BlobExpanded blobExpanded1 =
        BlobExpanded.newBuilder().setBlob(getBlob(path1)).addAllLocation(location1).build();

    String path2 = "/protos/proto/public/test.txt";
    List<String> location2 = new ArrayList<>();
    location2.add("modeldb");
    location2.add("environment.json");
    BlobExpanded blobExpanded2 =
        BlobExpanded.newBuilder().setBlob(getBlob(path2)).addAllLocation(location2).build();

    String path3 = "xyz.txt";
    List<String> location3 = new ArrayList<>();
    location3.add("modeldb.json");
    BlobExpanded blobExpanded3 =
        BlobExpanded.newBuilder().setBlob(getBlob(path3)).addAllLocation(location3).build();

    CreateCommitRequest createCommitRequest =
        CreateCommitRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .setCommit(
                Commit.newBuilder()
                    .setAuthor(authClientInterceptor.getClient1Email())
                    .setMessage("this is the test commit message")
                    .setDateCreated(Calendar.getInstance().getTimeInMillis())
                    .build())
            .addBlobs(blobExpanded1)
            .addBlobs(blobExpanded2)
            .addBlobs(blobExpanded3)
            .build();

    CreateCommitRequest.Response commitResponse =
        versioningServiceBlockingStub.createCommit(createCommitRequest);

    ListCommitBlobsRequest listCommitBlobsRequest =
        ListCommitBlobsRequest.newBuilder()
            .setCommitSha(commitResponse.getCommit().getCommitSha())
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .addLocationPrefix("modeldb")
            .build();

    ListCommitBlobsRequest.Response listCommitBlobsResponse =
        versioningServiceBlockingStub.listCommitBlobs(listCommitBlobsRequest);
    Assert.assertEquals(
        "blob count not match with expected blob count",
        2,
        listCommitBlobsResponse.getBlobsCount());
    Assert.assertEquals(
        "blob data not match with expected blob data",
        blobExpanded1,
        listCommitBlobsResponse.getBlobs(0));
    Assert.assertEquals(
        "blob data not match with expected blob data",
        blobExpanded2,
        listCommitBlobsResponse.getBlobs(1));

    listCommitBlobsRequest =
        ListCommitBlobsRequest.newBuilder()
            .setCommitSha(commitResponse.getCommit().getCommitSha())
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .addLocationPrefix("modeldb.json")
            .build();

    listCommitBlobsResponse = versioningServiceBlockingStub.listCommitBlobs(listCommitBlobsRequest);
    Assert.assertEquals(
        "blob count not match with expected blob count",
        1,
        listCommitBlobsResponse.getBlobsCount());
    Assert.assertEquals(
        "blob data not match with expected blob data",
        blobExpanded3,
        listCommitBlobsResponse.getBlobs(0));
    // TODO: Add Delete Commit code here

    DeleteRepositoryRequest deleteRepository =
        DeleteRepositoryRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id))
            .build();
    DeleteRepositoryRequest.Response deleteResult =
        versioningServiceBlockingStub.deleteRepository(deleteRepository);
    Assert.assertTrue(deleteResult.getStatus());

    LOGGER.info("List commit blob test end................................");
  }

  @Test
  public void getCommitBlobListUsecase2Test() {
    LOGGER.info("List commit blob test start................................");

    VersioningServiceBlockingStub versioningServiceBlockingStub =
        VersioningServiceGrpc.newBlockingStub(channel);

    SetRepository setRepository =
        SetRepository.newBuilder()
            .setId(
                RepositoryIdentification.newBuilder()
                    .setNamedId(
                        RepositoryNamedIdentification.newBuilder().setName("Repo-1").build())
                    .build())
            .setRepository(Repository.newBuilder().setName("Repo-1"))
            .build();
    SetRepository.Response result = versioningServiceBlockingStub.createRepository(setRepository);
    long id = result.getRepository().getId();

    String path1 = "/protos/proto/public/versioning/versioning.proto";
    List<String> location1 = new ArrayList<>();
    location1.add("modeldb");
    location1.add("environment");
    location1.add("march");
    location1.add("train.json"); // file
    BlobExpanded blobExpanded1 =
        BlobExpanded.newBuilder().setBlob(getBlob(path1)).addAllLocation(location1).build();

    String path2 = "/protos/proto/public/test.txt";
    List<String> location2 = new ArrayList<>();
    location2.add("modeldb");
    location2.add("environment");
    location2.add("environment.json");
    BlobExpanded blobExpanded2 =
        BlobExpanded.newBuilder().setBlob(getBlob(path2)).addAllLocation(location2).build();

    String path3 = "/protos/proto/public/test2.txt";
    List<String> location3 = new ArrayList<>();
    location3.add("modeldb");
    location3.add("dataset");
    location3.add("march");
    location3.add("dataset.json");
    BlobExpanded blobExpanded3 =
        BlobExpanded.newBuilder().setBlob(getBlob(path3)).addAllLocation(location3).build();

    String path4 = "xyz.txt";
    List<String> location4 = new ArrayList<>();
    location4.add("modeldb.json");
    BlobExpanded blobExpanded4 =
        BlobExpanded.newBuilder().setBlob(getBlob(path4)).addAllLocation(location4).build();

    CreateCommitRequest createCommitRequest =
        CreateCommitRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .setCommit(
                Commit.newBuilder()
                    .setAuthor(authClientInterceptor.getClient1Email())
                    .setMessage("this is the test commit message")
                    .setDateCreated(Calendar.getInstance().getTimeInMillis())
                    .build())
            .addBlobs(blobExpanded1)
            .addBlobs(blobExpanded2)
            .addBlobs(blobExpanded3)
            .addBlobs(blobExpanded4)
            .build();

    CreateCommitRequest.Response commitResponse =
        versioningServiceBlockingStub.createCommit(createCommitRequest);

    ListCommitBlobsRequest listCommitBlobsRequest =
        ListCommitBlobsRequest.newBuilder()
            .setCommitSha(commitResponse.getCommit().getCommitSha())
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .addLocationPrefix("modeldb")
            .addLocationPrefix("environment")
            .build();

    ListCommitBlobsRequest.Response listCommitBlobsResponse =
        versioningServiceBlockingStub.listCommitBlobs(listCommitBlobsRequest);
    Assert.assertEquals(
        "blob count not match with expected blob count",
        2,
        listCommitBlobsResponse.getBlobsCount());
    assertTrue(
        "blob data not match with expected blob data",
        listCommitBlobsResponse.getBlobsList().contains(blobExpanded1));
    assertTrue(
        "blob data not match with expected blob data",
        listCommitBlobsResponse.getBlobsList().contains(blobExpanded2));

    listCommitBlobsRequest =
        ListCommitBlobsRequest.newBuilder()
            .setCommitSha(commitResponse.getCommit().getCommitSha())
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .addLocationPrefix("modeldb")
            .addLocationPrefix("dataset")
            .build();

    listCommitBlobsResponse = versioningServiceBlockingStub.listCommitBlobs(listCommitBlobsRequest);
    Assert.assertEquals(
        "blob count not match with expected blob count",
        1,
        listCommitBlobsResponse.getBlobsCount());
    Assert.assertEquals(
        "blob data not match with expected blob data",
        blobExpanded3,
        listCommitBlobsResponse.getBlobs(0));

    listCommitBlobsRequest =
        ListCommitBlobsRequest.newBuilder()
            .setCommitSha(commitResponse.getCommit().getCommitSha())
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .addLocationPrefix("modeldb")
            .addLocationPrefix("march")
            .addLocationPrefix("dataset")
            .build();

    listCommitBlobsResponse = versioningServiceBlockingStub.listCommitBlobs(listCommitBlobsRequest);
    Assert.assertEquals(
        "blob count not match with expected blob count",
        0,
        listCommitBlobsResponse.getBlobsCount());

    listCommitBlobsRequest =
        ListCommitBlobsRequest.newBuilder()
            .setCommitSha(commitResponse.getCommit().getCommitSha())
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .addLocationPrefix("modeldb")
            .addLocationPrefix("dataset")
            .addLocationPrefix("march")
            .build();

    listCommitBlobsResponse = versioningServiceBlockingStub.listCommitBlobs(listCommitBlobsRequest);
    Assert.assertEquals(
        "blob count not match with expected blob count",
        1,
        listCommitBlobsResponse.getBlobsCount());
    Assert.assertEquals(
        "blob data not match with expected blob data",
        blobExpanded3,
        listCommitBlobsResponse.getBlobs(0));

    listCommitBlobsRequest =
        ListCommitBlobsRequest.newBuilder()
            .setCommitSha(commitResponse.getCommit().getCommitSha())
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .addLocationPrefix("modeldb")
            .build();

    listCommitBlobsResponse = versioningServiceBlockingStub.listCommitBlobs(listCommitBlobsRequest);
    Assert.assertEquals(
        "blob count not match with expected blob count",
        3,
        listCommitBlobsResponse.getBlobsCount());
    assertTrue(
        "blob data not match with expected blob data",
        listCommitBlobsResponse.getBlobsList().contains(blobExpanded1));
    assertTrue(
        "blob data not match with expected blob data",
        listCommitBlobsResponse.getBlobsList().contains(blobExpanded3));

    DeleteCommitRequest deleteCommitRequest =
        DeleteCommitRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .setCommitSha(commitResponse.getCommit().getCommitSha())
            .build();
    versioningServiceBlockingStub.deleteCommit(deleteCommitRequest);

    DeleteRepositoryRequest deleteRepository =
        DeleteRepositoryRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id))
            .build();
    DeleteRepositoryRequest.Response deleteResult =
        versioningServiceBlockingStub.deleteRepository(deleteRepository);
    Assert.assertTrue(deleteResult.getStatus());

    LOGGER.info("List commit blob test end................................");
  }
}
