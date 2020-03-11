package ai.verta.modeldb.versioning.blob.diff_factory;

import ai.verta.modeldb.versioning.Blob.ContentCase;
import ai.verta.modeldb.versioning.BlobDiff;
import ai.verta.modeldb.versioning.BlobDiff.Builder;
import ai.verta.modeldb.versioning.BlobExpanded;
import ai.verta.modeldb.versioning.DiffStatusEnum.DiffStatus;
import com.google.rpc.Code;
import com.google.rpc.Status;
import io.grpc.protobuf.StatusProto;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** constructs proto blob diff object from proto blobs*/
public abstract class BlobDiffFactory {

  private final BlobExpanded blobExpanded;

  BlobDiffFactory(BlobExpanded blobExpanded) {
    this.blobExpanded = blobExpanded;
  }

  private ContentCase getBlobType() {
    return blobExpanded.getBlob().getContentCase();
  }

  public BlobExpanded getBlobExpanded() {
    return blobExpanded;
  }

  public List<BlobDiff> compare(BlobDiffFactory blobDiffFactoryB, String location) {
    // TODO: Here used the `#` for split the locations but if folder
    // TODO: - contain the `#` then this functionality will break.
    final Builder builder = BlobDiff.newBuilder()
        .addAllLocation(Arrays.asList(location.split("#")));
    if (commonTypeEqual(blobDiffFactoryB)) {
      builder.setStatus(DiffStatus.MODIFIED);
      blobDiffFactoryB.delete(builder);
      add(builder);
      return Collections.singletonList(builder.build());
    } else {
      final Builder builder2 = builder.clone();
      builder.setStatus(DiffStatus.DELETED);
      blobDiffFactoryB.delete(builder);
      builder2.setStatus(DiffStatus.ADDED);
      add(builder2);
      return Stream.of(builder, builder2).map(Builder::build).collect(Collectors.toList());
    }
  }

  private boolean commonTypeEqual(BlobDiffFactory blobDiffFactory) {
    return blobDiffFactory.getBlobType() == getBlobType()
        && typeEqual(blobDiffFactory);
  }

  protected abstract boolean typeEqual(BlobDiffFactory blobDiffFactory);

  protected abstract void add(Builder builder);

  protected abstract void delete(Builder builder);

  public BlobDiff add(String location) {
    // TODO: Here used the `#` for split the locations but if folder
    // TODO: - contain the `#` then this functionality will break.
    final Builder builder = BlobDiff.newBuilder()
        .addAllLocation(Arrays.asList(location.split("#")));
    builder.setStatus(DiffStatus.ADDED);
    add(builder);
    return builder.build();
  }

  public BlobDiff delete(String location) {
    // TODO: Here used the `#` for split the locations but if folder
    // TODO: - contain the `#` then this functionality will break.
    final Builder builder = BlobDiff.newBuilder()
        .addAllLocation(Arrays.asList(location.split("#")));
    builder.setStatus(DiffStatus.DELETED);
    delete(builder);
    return builder.build();
  }

  public static BlobDiffFactory create(BlobExpanded blobExpanded) {
    switch (blobExpanded.getBlob().getContentCase()) {
      case ENVIRONMENT:
        return new EnvironmentBlobDiffFactory(blobExpanded);
      case CODE:
        return new CodeBlobDiffFactory(blobExpanded);
      case CONFIG:
        return new ConfigBlobDiffFactory(blobExpanded);
      case DATASET:
        return new DatasetBlobDiffFactory(blobExpanded);
      default:
        Status status =
            Status.newBuilder()
                .setCode(Code.INTERNAL_VALUE)
                .setMessage("Invalid blob type found")
                .build();
        throw StatusProto.toStatusRuntimeException(status);
    }
  }
}
