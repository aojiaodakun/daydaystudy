package com.hzk.java.net.nio;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * 关键概念解释
 * 容量(Capacity): 缓冲区能保存的最大数据量
 * 位置(Position): 下一个要读/写的位置
 * 限制(Limit): 缓冲区中第一个不能读/写的位置
 * 标记(Mark): 一个备忘位置，可通过reset()返回
 *
 * 方法	描述
 * allocate(int)	创建堆缓冲区
 * allocateDirect(int)	创建直接缓冲区(性能更高)
 * put(byte)	写入一个字节
 * get()	读取一个字节
 * flip()	切换为读模式
 * clear()	清空缓冲区
 * compact()	压缩缓冲区
 * rewind()	重设position为0
 * mark()	标记当前位置
 * reset()	回到标记位置
 */
public class NioByteBufferTest {

    public static void main(String[] args) {
        // 1. 创建ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(1024); // 分配堆内存
        // ByteBuffer directBuffer = ByteBuffer.allocateDirect(1024); // 分配直接内存

        // 2. 写入数据
        String message = "Hello, ByteBuffer!";
        buffer.put(message.getBytes(StandardCharsets.UTF_8));

        // 3. 从写模式切换到读模式
        buffer.flip();

        // 4. 读取数据
        byte[] readBytes = new byte[buffer.remaining()];
        buffer.get(readBytes);
        System.out.println("读取的内容: " + new String(readBytes, StandardCharsets.UTF_8));

        // 5. 清空缓冲区(准备重新写入)
        buffer.clear();

        // 6. 演示其他常用操作
        demonstrateOtherOperations();
    }

    private static void demonstrateOtherOperations() {
        System.out.println("\n其他操作演示:");

        ByteBuffer buf = ByteBuffer.allocate(20);
        System.out.println("初始状态 - position: " + buf.position() +
                ", limit: " + buf.limit() +
                ", capacity: " + buf.capacity());

        // 写入数据
        buf.put((byte)1);
        buf.put((byte)2);
        buf.put((byte)3);
        System.out.println("写入3字节后 - position: " + buf.position());

        // 切换到读模式
        buf.flip();
        System.out.println("flip()后 - position: " + buf.position() + ", limit: " + buf.limit());

        // 读取数据
        System.out.println("读取的数据: " + buf.get());
        System.out.println("读取1字节后 - position: " + buf.position());

        // 标记当前位置
        buf.mark();
        System.out.println("读取的数据: " + buf.get());
        System.out.println("再读取1字节后 - position: " + buf.position());

        // 重置到标记位置
        buf.reset();
        System.out.println("reset()后 - position: " + buf.position());

        // 压缩缓冲区
        buf.compact();
        System.out.println("compact()后 - position: " + buf.position() + ", limit: " + buf.limit());

        // 重新写入
        buf.put((byte)4);
        buf.put((byte)5);
        System.out.println("再写入2字节后 - position: " + buf.position());

        // 准备读取
        buf.flip();
        while(buf.hasRemaining()) {
            System.out.println("剩余数据: " + buf.get());
        }
    }
}
