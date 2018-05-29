package me.dslztx.assist.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * @author dslztx
 */
public class CodeAssist {

  /**
   * 以特定编码方案编码字符串，返回编码得到的字节流
   */
  public static byte[] encode(String str, CodeMethod codeMethod) {
    if (StringAssist.isBlank(str)) {
      return null;
    }
    if (codeMethod == null) {
      throw new RuntimeException("codeMethod is null");
    }

    try {
      return str.getBytes(codeMethod.getCharset());
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 以特定编码方案解码字节流，返回解码得到的字符串
   */
  public static String decode(byte[] bytes, CodeMethod codeMethod) {
    if (ArrayAssist.isEmpty(bytes)) {
      return null;
    }
    if (codeMethod == null) {
      throw new RuntimeException("codeMethod is null");
    }

    return new String(bytes, Charset.forName(codeMethod.getCharset()));
  }

  public static void main(String[] args) throws UnsupportedEncodingException {
    String a = "�";
    String b = CodeAssist.decode(CodeAssist.encode("�", CodeMethod.GBK), CodeMethod.GBK);
    System.out.println(a.equals(b));
  }

  /**
   * 常见编码方案枚举
   */
  public enum CodeMethod {
    UTF8("UTF-8"), GBK("GBK"), ISO("ISO"), ASCII("ASCII");

    private String charset;

    CodeMethod(String charset) {
      this.charset = charset;
    }

    public String getCharset() {
      return charset;
    }
  }
}