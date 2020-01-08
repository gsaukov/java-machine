package com.apps.cuda;

import jcuda.*;
import jcuda.runtime.*;

public class CudaTest {

    public static void main(String args[])
    {
        Pointer pointer = new Pointer();
        JCuda.cudaMalloc(pointer, 4);
        System.out.println("Pointer: "+pointer);
        JCuda.cudaFree(pointer);
    }
}
