package ai.verta.modeldb;

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
import ai.verta.modeldb.versioning.DeleteRepositoryRequest;
import ai.verta.modeldb.versioning.ListCommitsRequest;
import ai.verta.modeldb.versioning.PathDatasetBlob;
import ai.verta.modeldb.versioning.PathDatasetComponentBlob;
import ai.verta.modeldb.versioning.Repository;
import ai.verta.modeldb.versioning.RepositoryIdentification;
import ai.verta.modeldb.versioning.RepositoryNamedIdentification;
import ai.verta.modeldb.versioning.SetRepository;
import ai.verta.modeldb.versioning.SetRepository.Response;
import ai.verta.modeldb.versioning.VersioningServiceGrpc;
import ai.verta.modeldb.versioning.VersioningServiceGrpc.VersioningServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
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
public class CommitTest {

  private static final Logger LOGGER = LogManager.getLogger(CommitTest.class);
  public static final String NAME_2 = "name2";
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

  private PathDatasetComponentBlob getPathDatasetComponentBlob(String blobLocation) {
    return PathDatasetComponentBlob.newBuilder()
        .setPath(blobLocation)
        .setSize(2)
        .setLastModifiedAtSource(Calendar.getInstance().getTimeInMillis())
        .build();
  }

  private PathDatasetBlob getPathDatasetBlob() {
    return PathDatasetBlob.newBuilder()
        .addComponents(
            getPathDatasetComponentBlob("/protos/proto/public/versioning/versioning.proto"))
        .build();
  }

  private Blob getBlob() {
    DatasetBlob datasetBlob = DatasetBlob.newBuilder().setPath(getPathDatasetBlob()).build();
    return Blob.newBuilder().setDataset(datasetBlob).build();
  }

  private CreateCommitRequest getCreateCommitRequest(Long repoId, long commitTime) {

    Commit commit =
        Commit.newBuilder()
            .setAuthor(authClientInterceptor.getClient1Email())
            .setMessage("this is the test commit message")
            .setDateCreated(commitTime)
            .build();
    CreateCommitRequest createCommitRequest =
        CreateCommitRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(repoId).build())
            .setCommit(commit)
            .addBlobs(BlobExpanded.newBuilder().setBlob(getBlob()).addLocation("/").build())
            .build();

    return createCommitRequest;
  }

  @Test
  public void listCommitsTest() {
    LOGGER.info("List of commits test start................................");

    VersioningServiceBlockingStub versioningServiceBlockingStub =
        VersioningServiceGrpc.newBlockingStub(channel);

    SetRepository setRepository =
        SetRepository.newBuilder()
            .setId(
                RepositoryIdentification.newBuilder()
                    .setNamedId(RepositoryNamedIdentification.newBuilder().setName("name").build())
                    .build())
            .setRepository(Repository.newBuilder().setName("name"))
            .build();
    Response result = versioningServiceBlockingStub.createRepository(setRepository);
    long id = result.getRepository().getId();

    List<Commit> commitList = new ArrayList<>();
    CreateCommitRequest createCommitRequest = getCreateCommitRequest(id, 111);
    CreateCommitRequest.Response commitResponse =
        versioningServiceBlockingStub.createCommit(createCommitRequest);
    commitList.add(commitResponse.getCommit());
    createCommitRequest = getCreateCommitRequest(id, 123);
    commitResponse = versioningServiceBlockingStub.createCommit(createCommitRequest);
    commitList.add(commitResponse.getCommit());
    createCommitRequest = getCreateCommitRequest(id, 450);
    commitResponse = versioningServiceBlockingStub.createCommit(createCommitRequest);
    commitList.add(commitResponse.getCommit());
    createCommitRequest = getCreateCommitRequest(id, 500);
    commitResponse = versioningServiceBlockingStub.createCommit(createCommitRequest);
    commitList.add(commitResponse.getCommit());

    ListCommitsRequest listCommitsRequest =
        ListCommitsRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .build();
    ListCommitsRequest.Response listCommitsResponse =
        versioningServiceBlockingStub.listCommits(listCommitsRequest);
    Assert.assertEquals(
        "Commit count not match with the expected count", 4, listCommitsResponse.getCommitsCount());
    Assert.assertEquals(
        "Commit list not match with expected commit list",
        commitList,
        listCommitsResponse.getCommitsList());
    Assert.assertEquals(
        "Commit list not match with expected commit list",
        commitList.get(1),
        listCommitsResponse.getCommits(1));

    listCommitsRequest =
        ListCommitsRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .setCommitBase(commitList.get(1).getCommitSha())
            .build();
    listCommitsResponse = versioningServiceBlockingStub.listCommits(listCommitsRequest);
    Assert.assertEquals(
        "Commit count not match with the expected count", 3, listCommitsResponse.getCommitsCount());
    Assert.assertEquals(
        "Commit list not match with expected commit list",
        commitList.subList(1, commitList.size()),
        listCommitsResponse.getCommitsList());
    Assert.assertEquals(
        "Commit list not match with expected commit list",
        commitList.get(2),
        listCommitsResponse.getCommits(1));

    listCommitsRequest =
        ListCommitsRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .setCommitBase(commitList.get(1).getCommitSha())
            .setCommitHead(commitList.get(2).getCommitSha())
            .build();
    listCommitsResponse = versioningServiceBlockingStub.listCommits(listCommitsRequest);
    Assert.assertEquals(
        "Commit count not match with the expected count", 2, listCommitsResponse.getCommitsCount());
    Assert.assertEquals(
        "Commit list not match with expected commit list",
        commitList.subList(1, commitList.size() - 1),
        listCommitsResponse.getCommitsList());
    Assert.assertEquals(
        "Commit list not match with expected commit list",
        commitList.get(2),
        listCommitsResponse.getCommits(1));

    // TODO: DeleteCommit not implemented yet
    /*DeleteCommitRequest deleteCommitRequest =
        DeleteCommitRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id).build())
            .setCommitSha(commitResponse.getCommit().getFolderSha())
            .build();
    DeleteCommitRequest.Response deleteCommitResponse =
        versioningServiceBlockingStub.deleteCommit(deleteCommitRequest);*/

    DeleteRepositoryRequest deleteRepository =
        DeleteRepositoryRequest.newBuilder()
            .setRepositoryId(RepositoryIdentification.newBuilder().setRepoId(id))
            .build();
    DeleteRepositoryRequest.Response deleteResult =
        versioningServiceBlockingStub.deleteRepository(deleteRepository);
    Assert.assertTrue(deleteResult.getStatus());

    LOGGER.info("List of commits test end................................");
  }
}
