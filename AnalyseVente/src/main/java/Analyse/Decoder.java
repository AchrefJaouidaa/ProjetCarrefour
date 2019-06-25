package Analyse;

import java.nio.ByteBuffer;

/**
 * L'interface Decoder pour decoder le stream des données en lecture
 * @param <T> type des données à decoder
 */
public interface Decoder<T> {
  public T decode (ByteBuffer buffer);
}
