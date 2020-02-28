package ai.verta.modeldb.versioning;

import org.hibernate.Session;
import org.hibernate.query.Query;

public class VersioningUtils {
  private static final String COMMIT_BELONGS_TO_REPO_QUERY =
      "SELECT count(*) FROM repository_commit rc WHERE rc.commit_hash = :commitHash AND rc.repository_id = :repoId";

  /**
   * Checks the database and returns if a commitHash belongs to a repository
   *
   * @param session
   * @param commitHash : hash of commit
   * @param repositoryId : id of the repository
   * @return
   */
  static boolean commitRepositoryMappingExists(
      Session session, String commitHash, Long repositoryId) {
    Query query = session.createQuery(COMMIT_BELONGS_TO_REPO_QUERY);
    query.setParameter("commitHash", commitHash);
    query.setParameter("repoId", repositoryId);
    Long count = (Long) query.getSingleResult();
    return count > 0;
  }
}
