package ai.verta.modeldb.versioning;

import ai.verta.modeldb.entities.dataset.PathDatasetComponentBlobEntity;
import ai.verta.modeldb.entities.dataset.S3DatasetComponentBlobEntity;
import ai.verta.modeldb.utils.ModelDBHibernateUtil;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.hibernate.Session;

public class DatasetComponentDAORdbImpl implements DatasetComponentDAO {

  public String setBlobs(Session session, List<BlobExpanded> blobsList) {
      blobsList.forEach(blob -> {
        final DatasetBlob dataset = blob.getBlob().getDataset();
        switch (dataset.getContentCase()) {
          case S3:
            for (S3DatasetComponentBlob componentBlob: dataset.getS3().getComponentsList()) {
              S3DatasetComponentBlobEntity s3DatasetComponentBlobEntity = new S3DatasetComponentBlobEntity(UUID.randomUUID().toString(), componentBlob.getPath().getSha256(), componentBlob.getPath());
              session.saveOrUpdate(s3DatasetComponentBlobEntity);
            }
            break;
          case PATH:
            for (PathDatasetComponentBlob componentBlob: dataset.getPath().getComponentsList()) {
              PathDatasetComponentBlobEntity pathDatasetComponentBlobEntity = new PathDatasetComponentBlobEntity(
                  UUID.randomUUID().toString(), componentBlob.getSha256(), componentBlob);
              session.saveOrUpdate(pathDatasetComponentBlobEntity);
            }
            break;
        }
      });
  }
}
