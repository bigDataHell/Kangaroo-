##  1  Docker安装与启动







## 2 docker命令

###  启动docker：

`systemctl start docker`

### 停止docker：

`systemctl stop docker`

### 重启docker：

`systemctl restart docker`

###  查看docker状态：

`systemctl status docker`

### 开机启动：

`systemctl enable`

### 查看所有镜像

`docker images`

### l查看docker概要信息：

`docker info`

### 查看 docker帮助文档：

`docker --help`

## 3 docker容器操作

### 3.1 docker 容器命令

#### 查看正在运行容器：

`docker ps `

####  查看所有的容器（启动过的历史容器）：

`docker ps –a  `

####  查看最后有一次运行的容器

`docker ps -l`

#### 查看停止的容器

`docker ps -f status=exited`

### 3.2 docker创建与启动容器

#### 创建容器常用的参数说明：

* 创建容器命令：`docker run`
	* `-i`：表示运行容器

	* `-t`：表示容器启动后会进入其命令行。加入这两个参数后，容器创建就能登录进去。即分配一个伪终端。

	* `--name` :为创建的容器命名。

	* `-v`：表示目录映射关系（前者是宿主机目录，后者是映射到宿主机上的目录），可以使用多个－v做多个目录或文件映射。注意：最好做目录映射，在宿主机上做修改，然后共享到容器上。

	* `-d`：在run后面加上-d参数,则会创建一个守护式容器在后台运行（这样创建容器后不会自动登录容器，如果只加-i -t两个参数，创建后就会自动进去容器）。

	* `-p`：表示端口映射，前者是宿主机端口，后者是容器内的映射端口。可以使用多个－p做多个端口映射


####   交互式容器

创建一个交互式容器并取名为mycentos

`docker run -it --name=mycentos centos:7 /bin/bash`

这时我们通过ps命令查看，发现可以看到启动的容器，状态为启动状态

使用exit命令 退出当前容器,容器关机

启动之后登陆 : 

`docker exec -it mycentos  /bin/bash`

#### 守护式容器

创建一个守护式容器：如果对于一个需要长期运行的容器来说，我们可以创建一个守护式容器。命令如下（容器名称不能重复）：

`docker run -di --name=mycentos2 centos:7`

* 登录守护式容器方式：

`docker exec -it mycentos2/ID也行  /bin/bash`


### 3.3 停止与启动容器

#### 停止正在运行的容器：

`docker stop $CONTAINER_NAME/ID`

#### 启动已运行过的容器：

`docker start $CONTAINER_NAME/ID`

### 3.4  文件拷贝

如果我们需要将文件拷贝到容器内可以使用cp命令

`docker cp 需要拷贝的文件或目录 容器名称:容器目录`
` docker cp anaconda-ks.cfg  mycentos2:/`

也可以将文件从容器内拷贝出来

`docker cp 容器名称:容器目录 需要拷贝的文件或目录`
`docker cp mycentos2:/etc/yum /root`

### 3.5 目录挂载

我们可以在创建容器的时候，将宿主机的目录与容器内的目录进行映射，这样我们就可以通过修改宿主机某个目录的文件从而去影响容器。

将容器的某一个目录与宿主机某一个目录挂在

创建容器 添加`-v`参数 后边为   宿主机目录:容器目录

`docker run -di -v /usr/local/myhtml:/usr/local/myhtml --name=mycentos2 centos:7`

如果你共享的是多级的目录，可能会出现权限不足的提示。

这是因为CentOS7中的安全模块selinux把权限禁掉了，我们需要添加参数  --privileged=true  来解决挂载的目录没有权限的问题

`docker run -di --name=mycentos2 -v /usr/local/myhtml:/usr/local/myhtml   --privileged=true centos:7`

### 3.6 查看容器IP地址

我们可以通过以下命令查看容器运行的各种数据

`docker inspect mycentos2`

也可以直接执行下面的命令直接输出IP地址

`docker inspect --format='{{.NetworkSettings.IPAddress}}' mycentos2`

### 3.7 删除容器

* 删除指定的容器：

`docker rm $CONTAINER_ID/NAME`

__注意，只能删除停止的容器__

* 删除所有容器：

`docker rm `docker ps -a -q`



 



