package ai.verta.modeldb.versioning.blob.diff_factory;

import ai.verta.modeldb.versioning.BlobDiff.Builder;
import ai.verta.modeldb.versioning.BlobExpanded;
import ai.verta.modeldb.versioning.ConfigBlob;
import ai.verta.modeldb.versioning.ConfigDiff;
import ai.verta.modeldb.versioning.HyperparameterConfigDiff;
import ai.verta.modeldb.versioning.HyperparameterSetConfigDiff;

public class ConfigBlobDiffFactory extends BlobDiffFactory {

  public ConfigBlobDiffFactory(BlobExpanded blobExpanded) {
    super(blobExpanded);
  }

  @Override
  protected boolean subtypeEqual(BlobDiffFactory blobDiffFactory) {
    return true;
  }

  @Override
  protected void add(Builder blobBuilder) {
    modify(blobBuilder, true);
  }

  @Override
  protected void delete(Builder blobBuilder) {
    modify(blobBuilder, false);
  }

  private void modify(Builder blobBuilder, boolean add) {
    final ConfigDiff.Builder configBuilder = ConfigDiff.newBuilder();
    final ConfigBlob config = getBlobExpanded().getBlob().getConfig();
    HyperparameterConfigDiff.Builder hyperparameterBuilder;
    HyperparameterSetConfigDiff.Builder hyperparameterSetBuilder;
    if (blobBuilder.hasConfig()) {
      final ConfigDiff config1 = blobBuilder.getConfig();
      hyperparameterBuilder = config1.getHyperparameters().toBuilder();
      hyperparameterSetBuilder = config1.getHyperparameterSet().toBuilder();
    } else {
      hyperparameterBuilder = HyperparameterConfigDiff.newBuilder();
      hyperparameterSetBuilder = HyperparameterSetConfigDiff.newBuilder();
    }
    if (add) {
      hyperparameterBuilder.addAllA(config.getHyperparametersList());
      hyperparameterSetBuilder.addAllA(config.getHyperparameterSetList());
    } else {
      hyperparameterBuilder.addAllB(config.getHyperparametersList());
      hyperparameterSetBuilder.addAllB(config.getHyperparameterSetList());
    }

    configBuilder.setHyperparameters(hyperparameterBuilder);
    configBuilder.setHyperparameterSet(hyperparameterSetBuilder);
    blobBuilder.setConfig(configBuilder);
  }
}
