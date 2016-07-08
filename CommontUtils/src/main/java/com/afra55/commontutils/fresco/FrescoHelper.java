package com.afra55.commontutils.fresco;

/**
 * Created by yangshuai in the 14:33 of 2016.03.18 .
 *
 * 支持的 URI 有 http://www.fresco-cn.org/docs/supported-uris.html#_ <br/>
 * 远程图片：http://, https:// <br/>
 * 本地文件：file:// <br/>
 * Content provider： content:// <br/>
 * asset目录下的资源： asset:// <br/>
 * res目录下的资源： Uri uri = Uri.parse("res://包名(实际可以是任何字符串甚至留空)/" + R.drawable.ic_launcher);
 *
 */
public class FrescoHelper {

    private static final String TAG = "FrescoHelper";

    private FrescoHelper() {

    }

    public static FrescoHelper getInstance() {
        return new FrescoHelper();
    }



}
