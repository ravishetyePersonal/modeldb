package ai.verta.modeldb.versioning;

import org.hibernate.Session;

public interface BlobFunction {
  String apply(Session session);
}
