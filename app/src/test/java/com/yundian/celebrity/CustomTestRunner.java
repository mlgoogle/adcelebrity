//package com.yundian.celebrity;
//
//import org.junit.runners.model.InitializationError;
//import org.robolectric.annotation.Config;
//import org.robolectric.manifest.AndroidManifest;
//import org.robolectric.res.Fs;
//
//import RobolectricTestRunner;
//
///**
// * Created by sll on 2017/7/31.
// */
//
//public class CustomTestRunner extends RobolectricTestRunner {
//    private static final String BUILD_OUTPUT = "E:/myproject/xxx/app/build/intermediates/";
//    public CustomTestRunner(Class<?> testClass) throws InitializationError {
//        super(testClass);
//    }
//
//
//
//    @Override
//    protected AndroidManifest getAppManifest(Config config) {
//        String manifestPath = BUILD_OUTPUT + "manifests/full/debug/AndroidManifest.xml";
//        String resDir = BUILD_OUTPUT + "res/merged/debug";
//        String assetsDir = BUILD_OUTPUT + "assets/debug";
//
//        AndroidManifest manifest = createAppManifest(Fs.fileFromPath(manifestPath),
//                Fs.fileFromPath(resDir),
//                Fs.fileFromPath(assetsDir),"com.uthing");
//        return manifest;
//    }
//}}
