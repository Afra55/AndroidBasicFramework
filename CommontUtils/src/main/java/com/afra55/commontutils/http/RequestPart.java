package com.afra55.commontutils.http;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.ByteString;

/**
 * @author yangshuai
 * @date 2017/8/19
 * {link http://afra55.github.io}
 */

public class RequestPart {

    private Map<String, RequestBody> data = new HashMap<>();

    public static RequestPart.Build getBuildInstance() {
        return new RequestPart.Build();
    }

    public Map<String, RequestBody> getData() {
        return data;
    }

    public void setData(Map<String, RequestBody> data) {
        this.data = data;
    }


    public static class Build {
        private Map<String, RequestBody> buildData = new HashMap<>();

        public Build() {
        }

        public Build withParam(String param, RequestBody obj) {
            if (obj != null) {
                buildData.put(param, obj);
            }
            return this;
        }

        public Build withParam(String param, String mediaType, String file) {
            RequestBody requestBody = null;
            try {
                requestBody = RequestBody.create(MediaType.parse(mediaType), file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (requestBody != null) {
                buildData.put(param, requestBody);
            }
            return this;
        }

        public Build withParam(String param, String mediaType, ByteString file) {
            RequestBody requestBody = null;
            try {
                requestBody = RequestBody.create(MediaType.parse(mediaType), file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (requestBody != null) {
                buildData.put(param, requestBody);
            }
            return this;
        }

        public Build withParam(String param, String mediaType, byte[] file) {
            RequestBody requestBody = null;
            try {
                requestBody = RequestBody.create(MediaType.parse(mediaType), file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (requestBody != null) {
                buildData.put(param, requestBody);
            }
            return this;
        }

        public Build withParam(String param, String mediaType, final byte[] content,
                               final int offset, final int byteCount) {
            RequestBody requestBody = null;
            try {
                requestBody = RequestBody.create(MediaType.parse(mediaType), content, offset, byteCount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (requestBody != null) {
                buildData.put(param, requestBody);
            }
            return this;
        }

        public Build withParam(String param, String mediaType, File file) {
            RequestBody requestBody = null;
            try {
                requestBody = RequestBody.create(MediaType.parse(mediaType), file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (requestBody != null) {
                buildData.put(param, requestBody);
            }
            return this;
        }

        public Build withParam(String param, MediaType mediaType, String file) {
            RequestBody requestBody = null;
            try {
                requestBody = RequestBody.create(mediaType, file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (requestBody != null) {
                buildData.put(param, requestBody);
            }
            return this;
        }

        public Build withParam(String param, MediaType mediaType, ByteString file) {
            RequestBody requestBody = null;
            try {
                requestBody = RequestBody.create(mediaType, file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (requestBody != null) {
                buildData.put(param, requestBody);
            }
            return this;
        }

        public Build withParam(String param, MediaType mediaType, byte[] file) {
            RequestBody requestBody = null;
            try {
                requestBody = RequestBody.create(mediaType, file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (requestBody != null) {
                buildData.put(param, requestBody);
            }
            return this;
        }

        public Build withParam(String param, MediaType mediaType, final byte[] content,
                               final int offset, final int byteCount) {
            RequestBody requestBody = null;
            try {
                requestBody = RequestBody.create(mediaType, content, offset, byteCount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (requestBody != null) {
                buildData.put(param, requestBody);
            }
            return this;
        }

        public Build withParam(String param, MediaType mediaType, File file) {
            RequestBody requestBody = null;
            try {
                requestBody = RequestBody.create(mediaType, file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (requestBody != null) {
                buildData.put(param, requestBody);
            }
            return this;
        }

        public Map<String, RequestBody> build() {
            RequestPart query = new RequestPart();
            query.setData(buildData);
            return query.getData();
        }

    }
}
