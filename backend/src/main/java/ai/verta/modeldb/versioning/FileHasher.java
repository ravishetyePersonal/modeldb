package ai.verta.modeldb.versioning;

import com.google.protobuf.GeneratedMessageV3;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;

public class FileHasher {

  public String getSha(GeneratedMessageV3 path) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] hash = digest.digest(path.toString().getBytes(StandardCharsets.UTF_8));
    return new String(new Hex().encode(hash));
  }
}
