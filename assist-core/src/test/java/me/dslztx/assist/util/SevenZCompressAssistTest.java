package me.dslztx.assist.util;

import java.io.File;
import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SevenZCompressAssistTest {

  private static final Logger logger = LoggerFactory.getLogger(SevenZCompressAssistTest.class);

  private static final String PATH_PREFIX = "src/test/resources";

  @Test
  public void compressAndDecompress() throws Exception {
    try {
      File original = new File(PATH_PREFIX + File.separator + "a");
      File compressed = new File(PATH_PREFIX + File.separator + "test" + File.separator + "a.7z");
      compressed.getParentFile().mkdirs();

      SevenZCompressAssist.compress(original, compressed);

      SevenZCompressAssist.decompress(compressed, compressed.getParentFile());

      Assert.assertTrue(FileAssist.isDirSame(original,
          new File(compressed.getParentFile().getCanonicalPath() + File.separator + "a")));
    } catch (Exception e) {
      logger.error("", e);
      Assert.fail();
    } finally {
      FileAssist.delFileRecursiveForce(new File(PATH_PREFIX + File.separator + "test"));
    }
  }

  @Test
  public void compressAndDecompress2() throws Exception {
    try {
      File original = new File(PATH_PREFIX + File.separator + "1.xml");
      File compressed = new File(
          PATH_PREFIX + File.separator + "test" + File.separator + "1.xml.7z");
      compressed.getParentFile().mkdirs();

      SevenZCompressAssist.compress(original, compressed);

      SevenZCompressAssist.decompress(compressed, compressed.getParentFile());

      Assert.assertTrue(FileAssist.isContentSame(original,
          new File(compressed.getParentFile().getCanonicalPath() + File.separator + "1.xml")));
    } catch (Exception e) {
      logger.error("", e);
      Assert.fail();
    } finally {
      FileAssist.delFileRecursiveForce(new File(PATH_PREFIX + File.separator + "test"));
    }
  }
}