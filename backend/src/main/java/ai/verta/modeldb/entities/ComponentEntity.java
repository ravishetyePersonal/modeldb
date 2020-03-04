package ai.verta.modeldb.entities;

public interface ComponentEntity {

  void setBlobHash(String elementSha);

  void setBaseBlobHash(String folderHash);

  default boolean hasComponents(){return true;}

  default String getElementSha(){return null;}

  default String getElementName(){return null;}
}
