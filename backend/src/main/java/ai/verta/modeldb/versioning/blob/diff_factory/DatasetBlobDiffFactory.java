package ai.verta.modeldb.versioning.blob.diff_factory;

import ai.verta.modeldb.versioning.BlobDiff.Builder;
import ai.verta.modeldb.versioning.BlobExpanded;
import ai.verta.modeldb.versioning.DatasetBlob;
import ai.verta.modeldb.versioning.DatasetDiff;
import ai.verta.modeldb.versioning.PathDatasetDiff;
import ai.verta.modeldb.versioning.S3DatasetDiff;

public class DatasetBlobDiffFactory extends BlobDiffFactory {

  public DatasetBlobDiffFactory(BlobExpanded blobExpanded) {
    super(blobExpanded);
  }

  @Override
  protected boolean subtypeEqual(BlobDiffFactory blobDiffFactory) {
    return blobDiffFactory.getBlobExpanded().getBlob().getDataset().getContentCase()
        .equals(getBlobExpanded().getBlob().getDataset().getContentCase());
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
    final DatasetDiff.Builder datasetBuilder = DatasetDiff.newBuilder();
    final DatasetBlob dataset = getBlobExpanded().getBlob().getDataset();
    switch (dataset.getContentCase()) {
      case PATH:
        PathDatasetDiff.Builder pathBuilder;
        if (blobBuilder.hasDataset()) {
          pathBuilder = blobBuilder.getDataset().getPath().toBuilder();
        } else {
          pathBuilder = PathDatasetDiff.newBuilder();
        }
        if (add) {
          pathBuilder.addAllA(dataset.getPath().getComponentsList());
        } else {
          pathBuilder.addAllB(dataset.getPath().getComponentsList());
        }

        datasetBuilder.setPath(pathBuilder).build();
        break;
      case S3:
        S3DatasetDiff.Builder s3Builder;
        if (blobBuilder.hasDataset()) {
          s3Builder = blobBuilder.getDataset().getS3().toBuilder();
        } else {
          s3Builder = S3DatasetDiff.newBuilder();
        }
        if (add) {
          s3Builder.addAllA(dataset.getS3().getComponentsList());
        } else {
          s3Builder.addAllB(dataset.getS3().getComponentsList());
        }

        datasetBuilder.setS3(s3Builder).build();
        break;
    }
    blobBuilder.setDataset(datasetBuilder.build());
  }
}
