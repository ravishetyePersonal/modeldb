package ai.verta.modeldb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import ai.verta.modeldb.LineageEntryEnum.LineageEntryType;
import ai.verta.modeldb.LineageServiceGrpc.LineageServiceBlockingStub;
import ai.verta.modeldb.authservice.AuthService;
import ai.verta.modeldb.authservice.AuthServiceUtils;
import ai.verta.modeldb.authservice.PublicAuthServiceUtils;
import ai.verta.modeldb.authservice.PublicRoleServiceUtils;
import ai.verta.modeldb.authservice.RoleService;
import ai.verta.modeldb.authservice.RoleServiceUtils;
import ai.verta.modeldb.utils.ModelDBUtils;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.Status.Code;
import io.grpc.StatusRuntimeException;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.CoreMatchers;
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
public class LineageTest {

  private static final Logger LOGGER = LogManager.getLogger(LineageTest.class);
  private static final LineageEntry.Builder NOT_EXISTENT_DATASET =
      LineageEntry.newBuilder()
          .setType(LineageEntryType.DATASET_VERSION)
          .setExternalId("id_not_existent_dataset");
  private static final LineageEntry.Builder INPUT_DATASET =
      LineageEntry.newBuilder()
          .setType(LineageEntryType.DATASET_VERSION)
          .setExternalId("id_input_dataset");
  private static final LineageEntry.Builder INPUT_DATASET2 =
      LineageEntry.newBuilder()
          .setType(LineageEntryType.DATASET_VERSION)
          .setExternalId("id_input_dataset2");
  private static final LineageEntry.Builder INPUT_OUTPUT_EXP =
      LineageEntry.newBuilder()
          .setType(LineageEntryType.EXPERIMENT_RUN)
          .setExternalId("id_input_output_exp");
  private static final LineageEntry.Builder INPUT_EXP =
      LineageEntry.newBuilder()
          .setType(LineageEntryType.EXPERIMENT_RUN)
          .setExternalId("id_input_exp");
  private static final LineageEntry.Builder OUTPUT_DATASET =
      LineageEntry.newBuilder()
          .setType(LineageEntryType.DATASET_VERSION)
          .setExternalId("id_output_dataset");
  private static final LineageEntry.Builder OUTPUT_EXP =
      LineageEntry.newBuilder()
          .setType(LineageEntryType.EXPERIMENT_RUN)
          .setExternalId("id_output_exp");
  /**
   * This rule manages automatic graceful shutdown for the registered servers and channels at the
   * end of test.
   */
  @Rule public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

  private ManagedChannel channel = null;
  private static String serverName = InProcessServerBuilder.generateName();
  private static InProcessServerBuilder serverBuilder =
      InProcessServerBuilder.forName(serverName).directExecutor();
  private static InProcessChannelBuilder channelBuilder =
      InProcessChannelBuilder.forName(serverName).directExecutor();
  private LineageServiceBlockingStub lineageServiceStub;

  @SuppressWarnings("unchecked")
  @BeforeClass
  public static void setServerAndService() throws Exception {

    Map<String, Object> propertiesMap =
        ModelDBUtils.readYamlProperties(System.getenv(ModelDBConstants.VERTA_MODELDB_CONFIG));
    Map<String, Object> testPropMap = (Map<String, Object>) propertiesMap.get("test");
    Map<String, Object> databasePropMap = (Map<String, Object>) testPropMap.get("test-database");

    App app = App.getInstance();
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
  }

  @Before
  public void initializeChannel() throws IOException {
    grpcCleanup.register(serverBuilder.build().start());
    channel = grpcCleanup.register(channelBuilder.maxInboundMessageSize(1024).build());
    lineageServiceStub = LineageServiceGrpc.newBlockingStub(channel);
  }

  @Test
  public void createAndDeleteLineageNegativeTest() {
    AddLineage.Builder addLineage =
        AddLineage.newBuilder()
            .addInput(INPUT_DATASET)
            .addInput(LineageEntry.newBuilder().setType(LineageEntryType.DATASET_VERSION))
            .addOutput(INPUT_OUTPUT_EXP);
    try {
      Assert.assertFalse(lineageServiceStub.addLineage(addLineage.build()).getStatus());
      fail();
    } catch (StatusRuntimeException e) {
      Status status = Status.fromThrowable(e);
      Assert.assertEquals(Code.INVALID_ARGUMENT, status.getCode());
      Assert.assertNotNull(status.getDescription());
      Assert.assertThat(
          status.getDescription().toLowerCase(), CoreMatchers.containsString("external"));
      Assert.assertThat(status.getDescription().toLowerCase(), CoreMatchers.containsString("id"));
      Assert.assertThat(
          status.getDescription().toLowerCase(), CoreMatchers.containsString("empty"));
    }

    addLineage =
        AddLineage.newBuilder()
            .addInput(INPUT_DATASET)
            .addInput(INPUT_DATASET2)
            .addOutput(
                LineageEntry.newBuilder()
                    .setType(LineageEntryType.UNKNOWN)
                    .setExternalId("id_input_output_exp"));
    try {
      Assert.assertFalse(lineageServiceStub.addLineage(addLineage.build()).getStatus());
      fail();
    } catch (StatusRuntimeException e) {
      Status status = Status.fromThrowable(e);
      Assert.assertEquals(Code.INVALID_ARGUMENT, status.getCode());
      Assert.assertNotNull(status.getDescription());
      Assert.assertThat(
          status.getDescription().toLowerCase(), CoreMatchers.containsString("unknown"));
      Assert.assertThat(status.getDescription().toLowerCase(), CoreMatchers.containsString("type"));
    }
  }

  @Test
  public void createAndDeleteLineageTest() {
    LOGGER.info("Create and delete Lineage test start................................");

    try {
      check(
          Arrays.asList(INPUT_EXP, NOT_EXISTENT_DATASET),
          Arrays.asList(null, null),
          Arrays.asList(null, null));

      AddLineage.Builder addLineage =
          AddLineage.newBuilder()
              .addInput(INPUT_DATASET)
              .addInput(INPUT_DATASET2)
              .addOutput(INPUT_OUTPUT_EXP);
      Assert.assertTrue(lineageServiceStub.addLineage(addLineage.build()).getStatus());

      check(
          Arrays.asList(
              INPUT_OUTPUT_EXP, INPUT_DATASET, INPUT_DATASET2, INPUT_EXP, NOT_EXISTENT_DATASET),
          Arrays.asList(Arrays.asList(INPUT_DATASET, INPUT_DATASET2), null, null, null, null),
          Arrays.asList(
              null,
              Collections.singletonList(INPUT_OUTPUT_EXP),
              Collections.singletonList(INPUT_OUTPUT_EXP),
              null,
              null));

      addLineage =
          AddLineage.newBuilder()
              .addOutput(OUTPUT_DATASET)
              .addOutput(OUTPUT_EXP)
              .addInput(INPUT_EXP)
              .addInput(INPUT_OUTPUT_EXP);
      Assert.assertTrue(lineageServiceStub.addLineage(addLineage.build()).getStatus());

      check(
          Arrays.asList(
              INPUT_OUTPUT_EXP,
              INPUT_DATASET,
              INPUT_DATASET2,
              INPUT_EXP,
              OUTPUT_DATASET,
              OUTPUT_EXP),
          Arrays.asList(
              Arrays.asList(INPUT_DATASET, INPUT_DATASET2),
              null,
              null,
              null,
              Arrays.asList(INPUT_EXP, INPUT_OUTPUT_EXP),
              Arrays.asList(INPUT_EXP, INPUT_OUTPUT_EXP)),
          Arrays.asList(
              Arrays.asList(OUTPUT_DATASET, OUTPUT_EXP),
              Collections.singletonList(INPUT_OUTPUT_EXP),
              Collections.singletonList(INPUT_OUTPUT_EXP),
              Arrays.asList(OUTPUT_DATASET, OUTPUT_EXP),
              null,
              null));

      DeleteLineage.Builder deleteLineage =
          DeleteLineage.newBuilder()
              .addInput(INPUT_EXP)
              .addOutput(OUTPUT_DATASET)
              .addOutput(OUTPUT_EXP);
      Assert.assertTrue(lineageServiceStub.deleteLineage(deleteLineage.build()).getStatus());

      check(
          Arrays.asList(
              INPUT_OUTPUT_EXP,
              INPUT_DATASET,
              INPUT_DATASET2,
              INPUT_EXP,
              OUTPUT_DATASET,
              OUTPUT_EXP),
          Arrays.asList(
              Arrays.asList(INPUT_DATASET, INPUT_DATASET2),
              null,
              null,
              null,
              Collections.singletonList(INPUT_OUTPUT_EXP),
              Collections.singletonList(INPUT_OUTPUT_EXP)),
          Arrays.asList(
              Arrays.asList(OUTPUT_DATASET, OUTPUT_EXP),
              Collections.singletonList(INPUT_OUTPUT_EXP),
              Collections.singletonList(INPUT_OUTPUT_EXP),
              null,
              null,
              null));

      deleteLineage =
          DeleteLineage.newBuilder()
              .addInput(INPUT_EXP)
              .addInput(INPUT_DATASET)
              .addInput(INPUT_DATASET2)
              .addInput(INPUT_EXP)
              .addInput(INPUT_OUTPUT_EXP)
              .addOutput(INPUT_OUTPUT_EXP)
              .addOutput(OUTPUT_DATASET)
              .addOutput(OUTPUT_EXP);
      Assert.assertTrue(lineageServiceStub.deleteLineage(deleteLineage.build()).getStatus());

      check(
          Arrays.asList(
              INPUT_OUTPUT_EXP,
              INPUT_DATASET,
              INPUT_DATASET2,
              INPUT_EXP,
              OUTPUT_DATASET,
              OUTPUT_EXP),
          Arrays.asList(null, null, null, null, null, null),
          Arrays.asList(null, null, null, null, null, null));
    } catch (StatusRuntimeException e) {
      Status status = Status.fromThrowable(e);
      LOGGER.warn("Error Code : " + status.getCode() + " Description : " + status.getDescription());
      e.printStackTrace();
      fail();
    }

    LOGGER.info("Create and delete Lineage test stop................................");
  }

  private static LineageEntryBatch formatToBatch(List<LineageEntry.Builder> x) {
    LineageEntryBatch.Builder batch = LineageEntryBatch.newBuilder();
    if (x != null) {
      batch.addAllItems(
          x.stream().map(LineageEntry.Builder::build).sorted(LineageTest::compare)::iterator);
    }
    return batch.build();
  }

  private static LineageEntryBatch sortFormattedArray(LineageEntryBatch x) {
    return x.newBuilderForType()
        .addAllItems(
            x.getItemsList().stream().sorted(LineageTest::compare).collect(Collectors.toList()))
        .build();
  }

  private static int compare(LineageEntry o1, LineageEntry o2) {
    final int i = o1.getExternalId().compareTo(o2.getExternalId());
    if (i == 0) {
      return o1.getType().compareTo(o2.getType());
    }
    return i;
  }

  private void check(
      List<LineageEntry.Builder> data,
      List<List<LineageEntry.Builder>> expectedInputs,
      List<List<LineageEntry.Builder>> expectedOutputs) {

    List<LineageEntry> iterator =
        data.stream().map(LineageEntry.Builder::build).collect(Collectors.toList());
    FindAllInputs.Response findAllInputsResult =
        lineageServiceStub.findAllInputs(FindAllInputs.newBuilder().addAllItems(iterator).build());
    List<LineageEntryBatch> expectedInputsWithoutNulls =
        expectedInputs.stream().map(LineageTest::formatToBatch).collect(Collectors.toList());
    final List<LineageEntryBatch> collect =
        findAllInputsResult.getInputsList().stream()
            .map(LineageTest::sortFormattedArray)
            .collect(Collectors.toList());
    assertEquals(expectedInputsWithoutNulls, collect);
    FindAllOutputs.Response findAllOutputsResult =
        lineageServiceStub.findAllOutputs(
            FindAllOutputs.newBuilder().addAllItems(iterator).build());
    List<LineageEntryBatch> expectedOutputsWithoutNulls =
        expectedOutputs.stream().map(LineageTest::formatToBatch).collect(Collectors.toList());
    assertEquals(
        expectedOutputsWithoutNulls,
        findAllOutputsResult.getOutputsList().stream()
            .map(LineageTest::sortFormattedArray)
            .collect(Collectors.toList()));
    FindAllInputsOutputs.Response findAllInputsOutputsResult =
        lineageServiceStub.findAllInputsOutputs(
            FindAllInputsOutputs.newBuilder().addAllItems(iterator).build());
    assertEquals(
        expectedInputsWithoutNulls,
        findAllInputsOutputsResult.getInputsList().stream()
            .map(LineageTest::sortFormattedArray)
            .collect(Collectors.toList()));
    assertEquals(
        expectedOutputsWithoutNulls,
        findAllInputsOutputsResult.getOutputsList().stream()
            .map(LineageTest::sortFormattedArray)
            .collect(Collectors.toList()));
  }
}
