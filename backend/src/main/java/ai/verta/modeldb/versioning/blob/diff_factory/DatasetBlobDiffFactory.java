package ai.verta.modeldb.versioning.blob.diff_factory;

import static ai.verta.modeldb.versioning.blob.diff_factory.ConfigBlobDiffFactory.removeCommon;

import ai.verta.modeldb.versioning.BlobDiff;
import ai.verta.modeldb.versioning.BlobExpanded;
import ai.verta.modeldb.versioning.DatasetBlob;
import ai.verta.modeldb.versioning.DatasetDiff;
import ai.verta.modeldb.versioning.PathDatasetComponentBlob;
import ai.verta.modeldb.versioning.PathDatasetDiff;
import ai.verta.modeldb.versioning.S3DatasetComponentBlob;
import ai.verta.modeldb.versioning.S3DatasetDiff;
import java.util.HashSet;
import java.util.Set;

public class DatasetBlobDiffFactory extends BlobDiffFactory {

  public DatasetBlobDiffFactory(BlobExpanded blobExpanded) {
    super(blobExpanded);
  }

  @Override
  protected boolean subtypeEqual(BlobDiffFactory blobDiffFactory) {
    return blobDiffFactory
        .getBlobExpanded()
        .getBlob()
        .getDataset()
        .getContentCase()
        .equals(getBlobExpanded().getBlob().getDataset().getContentCase());
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
    final DatasetDiff.Builder datasetBuilder = DatasetDiff.newBuilder();
    final DatasetBlob dataset = getBlobExpanded().getBlob().getDataset();
    switch (dataset.getContentCase()) {
      case PATH:
        PathDatasetDiff.Builder pathBuilder;
        if (blobDiffBuilder.hasDataset()) {
          pathBuilder = blobDiffBuilder.getDataset().getPath().toBuilder();
        } else {
          pathBuilder = PathDatasetDiff.newBuilder();
        }
        if (add) {
          if (pathBuilder.getBCount() != 0) {
            Set<PathDatasetComponentBlob> pathDatasetComponentBlobsA =
                new HashSet<>(dataset.getPath().getComponentsList());
            HashSet<PathDatasetComponentBlob> pathDatasetComponentBlobsB =
                new HashSet<>(pathBuilder.getBList());
            removeCommon(pathDatasetComponentBlobsA, pathDatasetComponentBlobsB);
            pathBuilder.clear();
            pathBuilder.addAllA(pathDatasetComponentBlobsA);
            pathBuilder.addAllB(pathDatasetComponentBlobsB);
          } else {
            pathBuilder.addAllA(dataset.getPath().getComponentsList());
          }
        } else {
          pathBuilder.addAllB(dataset.getPath().getComponentsList());
        }

        datasetBuilder.setPath(pathBuilder).build();
        break;
      case S3:
        S3DatasetDiff.Builder s3Builder;
        if (blobDiffBuilder.hasDataset()) {
          s3Builder = blobDiffBuilder.getDataset().getS3().toBuilder();
        } else {
          s3Builder = S3DatasetDiff.newBuilder();
        }
        if (add) {
          if (s3Builder.getBCount() != 0) {
            Set<S3DatasetComponentBlob> s3DatasetComponentBlobsA =
                new HashSet<>(dataset.getS3().getComponentsList());
            HashSet<S3DatasetComponentBlob> s3DatasetComponentBlobsB =
                new HashSet<>(s3Builder.getBList());
            removeCommon(s3DatasetComponentBlobsA, s3DatasetComponentBlobsB);
            s3Builder.clear();
            s3Builder.addAllA(s3DatasetComponentBlobsA);
            s3Builder.addAllB(s3DatasetComponentBlobsB);
          } else {
            s3Builder.addAllA(dataset.getS3().getComponentsList());
          }
        } else {
          s3Builder.addAllB(dataset.getS3().getComponentsList());
        }

        datasetBuilder.setS3(s3Builder).build();
        break;
    }
    blobDiffBuilder.setDataset(datasetBuilder.build());
  }
}
