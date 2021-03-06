package me.dslztx.assist.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ArrayAssistTest {

  @Test
  public void isEmpty() throws Exception {
    try {
      assertTrue(ArrayAssist.isEmpty(new String[0]));
      assertFalse(ArrayAssist.isEmpty(new String[1]));
    } catch (Exception e) {
      fail();
    }
  }
}