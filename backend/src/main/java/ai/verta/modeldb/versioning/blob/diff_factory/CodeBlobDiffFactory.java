package ai.verta.modeldb.versioning.blob.diff_factory;

import ai.verta.modeldb.versioning.BlobDiff.Builder;
import ai.verta.modeldb.versioning.BlobExpanded;
import ai.verta.modeldb.versioning.CodeBlob;
import ai.verta.modeldb.versioning.CodeDiff;
import ai.verta.modeldb.versioning.GitCodeDiff;
import ai.verta.modeldb.versioning.NotebookCodeDiff;

public class CodeBlobDiffFactory extends BlobDiffFactory {

  public CodeBlobDiffFactory(BlobExpanded blobExpanded) {
    super(blobExpanded);
  }

  @Override
  protected boolean typeEqual(BlobDiffFactory blobDiffFactory) {
    return blobDiffFactory.getBlobExpanded().getBlob().getCode().getContentCase()
        .equals(getBlobExpanded().getBlob().getCode().getContentCase());
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
    final CodeDiff.Builder codeBuilder = CodeDiff.newBuilder();
    final CodeBlob code = getBlobExpanded().getBlob().getCode();
    switch (code.getContentCase()) {
      case GIT:
        GitCodeDiff.Builder gitBuilder;
        if (blobBuilder.hasCode()) {
          gitBuilder = blobBuilder.getCode().getGit().toBuilder();
        } else {
          gitBuilder = GitCodeDiff.newBuilder();
        }
        if (add)
          gitBuilder.setA(code.getGit());
        else
          gitBuilder.setB(code.getGit());
            
        codeBuilder.setGit(gitBuilder).build();
        break;
      case NOTEBOOK:
        NotebookCodeDiff.Builder notebookBuilder;
        if (blobBuilder.hasCode()) {
          notebookBuilder = blobBuilder.getCode().getNotebook().toBuilder();
        } else {
          notebookBuilder = NotebookCodeDiff.newBuilder();
        }
        if (add)
          notebookBuilder.setA(code.getNotebook());
        else
          notebookBuilder.setB(code.getNotebook());

        codeBuilder.setNotebook(notebookBuilder).build();
        break;
    }
    blobBuilder.setCode(codeBuilder.build());
  }
}
