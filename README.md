<!--
 ___ _            _ _    _ _    __
/ __(_)_ __  _ __| (_)__(_) |_ /_/
\__ \ | '  \| '_ \ | / _| |  _/ -_)
|___/_|_|_|_| .__/_|_\__|_|\__\___|
            |_| 
-->
![](https://platform.simplicite.io/logos/standard/logo250.png)
* * *

`Minio` module definition
=========================

Minio S3 clouyd storage example
-------------------------------

### Prerequisites

1. Install Minio server

See [this page](https://docs.min.io/community/minio-object-store/index.html).
You should install both the server and the `mc` CLI client.

For a local deployment the storage server's S3 endpoint is `http://127.0.0.1:9000`

You can alias it as `local`:

```text
mc alias set local http://127.0.0.1:9000 minioadmin minioadmin
mc admin info local
```

> note: the objects browser UI is also available on `http://127.0.0.1:9001`.

2. Create credentials

```text
mc admin accesskey create local
```

The result should look like:

```text
Access Key: <a random access key>
Secret Key: <a random secret key>
Expiration: NONE
Name:
Description:
```

3. Create a `test` bucket

```text
mc mb local/test
```

4. Review the `MINIO_CONFIG` system param to comply with the above Minio server configuration

`MinioTest` business object definition
--------------------------------------



### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `minioTstCode`                                               | char(50)                                 | yes*     | yes       |          | -                                                                                |
| `minioTstLabel`                                              | char(255)                                |          | yes       |          | -                                                                                |
| `minioTstDocument`                                           | document                                 |          | yes       |          | -                                                                                |

