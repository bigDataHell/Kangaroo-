## 1 常用命令

### CenOS7 永居修改主机名

`hostnamectl set-hostname xxx`

### 查看当前登陆用户

`whoami`

### 修改密码 

`passwd`

### centos7正确关机重启 

         shutdown -h 10          #计算机将于10分钟后关闭，且会显示在登录用户的当前屏幕中
         shutdown -h now         #计算机会立刻关机
         shutdown -h 22:22       #计算机会在这个时刻关机
         shutdown -r now         #计算机会立刻重启
         shutdown -r +10         #计算机会将于10分钟后重启
         reboot                  #重启
         halt                    #关机
         
         
###  安装VIM

`yum -y install vim*`

### 
