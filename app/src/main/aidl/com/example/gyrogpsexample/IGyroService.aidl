// IGyroService.aidl
package com.example.gyrogpsexample;

// Declare any non-default types here with import statements

interface IGyroService {

    float getXAxisGyroValue();
    float getYAxisGyroValue();
    float getZAxisGyroValue();

    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);


}
