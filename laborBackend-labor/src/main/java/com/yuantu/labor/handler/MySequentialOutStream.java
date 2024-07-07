package com.yuantu.labor.handler;

import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.SevenZipException;

import java.io.FileOutputStream;
import java.io.IOException;

public class MySequentialOutStream implements ISequentialOutStream {

    private final FileOutputStream outputStream;

    public MySequentialOutStream(FileOutputStream outputStream) {
        this.outputStream = outputStream;
    }


    @Override
    public int write(byte[] data) throws SevenZipException {
        try {
            outputStream.write(data);
            return data.length;
        } catch (Exception e) {
            throw new SevenZipException(e);
        }
    }


    public void close() throws IOException {
        outputStream.close();
    }



}
