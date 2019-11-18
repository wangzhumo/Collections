## [Mars](https://github.com/Tencent/mars)

### Xlog 加密使用指引

####加密

1. 下载 [pyelliptic1.5.7](https://github.com/yann2192/pyelliptic/releases/tag/1.5.7)
2. 解压执行：`python setup.py install` 安装 pyelliptic1.5.7 注：如果没权限`sudo python setup.py install`



在 mars\log\crypt 下执行` python gen_key.py` 如果能生成成功则表示配置成功。 `python gen_key.py`会生成private key 和public key,把pulic key作为appender_open 函数参数设置进去，private key务必保存在安全的位置，防止泄露。并把这两个key设置到 mars\log\crypt 中 [decode_mars_crypt_log_file.py](https://github.com/Tencent/mars/blob/master/mars/log/crypt/decode_mars_crypt_log_file.py)脚本中。



#### 解密

1. 下载 [pyelliptic1.5.7](https://github.com/yann2192/pyelliptic/releases/tag/1.5.7)

2. 解压执行：`python setup.py install` 安装 pyelliptic1.5.7 

3. 执行 `python decode_mars_crypt_log_file.py [filepath]`

   ```
   eg:
   python decode_mars_crypt_log_file.py ~/Downloads/inyu_20190619.xlog
   
   而后会生成一个inyu_20190619.xlog.log
   ```

   





### Mars Android 接入指南

所有的编译脚本都在[mars/mars 目录](https://github.com/Tencent/mars/tree/master/mars/), 运行编译脚本之前也必须cd到此目录，在当前目录下运行，默认是编译 armeabi 的，如果需要其他 CPU 架构，把编译脚本中的`archs = set(['armeabi'])`稍作修改即可。

```
python build_android.py
```

执行命令后，会让选择:

```
Enter menu:
1. Clean && build mars.
2. Build incrementally mars.
3. Clean && build xlog.
4. Exit

这里选择3
```

把 mars_android_sdk/src 目录下的 Java 文件以及 libs/ 复制到的项目 