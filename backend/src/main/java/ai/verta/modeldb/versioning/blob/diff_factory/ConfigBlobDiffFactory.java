package ai.verta.modeldb.versioning.blob.diff_factory;

import ai.verta.modeldb.versioning.BlobDiff;
import ai.verta.modeldb.versioning.BlobExpanded;
import ai.verta.modeldb.versioning.ConfigBlob;
import ai.verta.modeldb.versioning.ConfigDiff;
import ai.verta.modeldb.versioning.HyperparameterConfigBlob;
import ai.verta.modeldb.versioning.HyperparameterConfigDiff;
import ai.verta.modeldb.versioning.HyperparameterSetConfigBlob;
import ai.verta.modeldb.versioning.HyperparameterSetConfigDiff;
import java.util.HashSet;
import java.util.Set;

public class ConfigBlobDiffFactory extends BlobDiffFactory {

  public ConfigBlobDiffFactory(BlobExpanded blobExpanded) {
    super(blobExpanded);
  }

  @Override
  protected boolean subtypeEqual(BlobDiffFactory blobDiffFactory) {
    return true;
  }

  @Override
  protected void add(BlobDiff.Builder blobDiffBuilder) {
    modify(blobDiffBuilder, true);
  }

  @Override
  protected void delete(BlobDiff.Builder blobDiffBuilder) {
    modify(blobDiffBuilder, false);
  }

  private void modify(BlobDiff.Builder blobDiffBuilder, boolean add) {
    final ConfigDiff.Builder configBuilder = ConfigDiff.newBuilder();
    final ConfigBlob config = getBlobExpanded().getBlob().getConfig();
    HyperparameterConfigDiff.Builder hyperparameterBuilder;
    HyperparameterSetConfigDiff.Builder hyperparameterSetBuilder;
    if (blobDiffBuilder.hasConfig()) {
      final ConfigDiff config1 = blobDiffBuilder.getConfig();
      hyperparameterBuilder = config1.getHyperparameters().toBuilder();
      hyperparameterSetBuilder = config1.getHyperparameterSet().toBuilder();
    } else {
      hyperparameterBuilder = HyperparameterConfigDiff.newBuilder();
      hyperparameterSetBuilder = HyperparameterSetConfigDiff.newBuilder();
    }
    if (add) {
      if (hyperparameterBuilder.getBCount() != 0) {
        Set<HyperparameterConfigBlob> hyperparameterConfigBlobsA =
            new HashSet<>(config.getHyperparametersList());
        HashSet<HyperparameterConfigBlob> hyperparameterConfigBlobsB =
            new HashSet<>(hyperparameterBuilder.getBList());
        removeCommon(hyperparameterConfigBlobsA, hyperparameterConfigBlobsB);
        hyperparameterBuilder.clear();
        hyperparameterBuilder.addAllA(hyperparameterConfigBlobsA);
        hyperparameterBuilder.addAllB(hyperparameterConfigBlobsB);
      } else {
        hyperparameterBuilder.addAllA(config.getHyperparametersList());
      }
      if (hyperparameterBuilder.getBCount() != 0) {
        Set<HyperparameterSetConfigBlob> hyperparameterSetConfigBlobsA =
            new HashSet<>(config.getHyperparameterSetList());
        HashSet<HyperparameterSetConfigBlob> hyperparameterSetConfigBlobsB =
            new HashSet<>(hyperparameterSetBuilder.getBList());
        removeCommon(hyperparameterSetConfigBlobsA, hyperparameterSetConfigBlobsB);
        hyperparameterSetBuilder.clear();
        hyperparameterSetBuilder.addAllA(hyperparameterSetConfigBlobsA);
        hyperparameterSetBuilder.addAllB(hyperparameterSetConfigBlobsB);
      } else {
        hyperparameterSetBuilder.addAllA(config.getHyperparameterSetList());
      }
    } else {
      hyperparameterBuilder.addAllB(config.getHyperparametersList());
      hyperparameterSetBuilder.addAllB(config.getHyperparameterSetList());
    }

    configBuilder.setHyperparameters(hyperparameterBuilder);
    configBuilder.setHyperparameterSet(hyperparameterSetBuilder);
    blobDiffBuilder.setConfig(configBuilder);
  }

  static <T> void removeCommon(Set<T> componentsBlobsA,
      Set<T> componentsBlobsB) {
    Set<T> commonElements =
        new HashSet<>(componentsBlobsA);
    commonElements.retainAll(componentsBlobsB);
    componentsBlobsA.removeAll(commonElements);
    componentsBlobsB.removeAll(commonElements);
  }
}
