:: Turn off Echo
@echo off
:: Set the path to the current directory
set path_application=%cd%
set path_nginx_server=%path_application%\nginx-1.23.2\html
set path_file_github=%path_application%\application\
set path_nginx_config=%path_application%\nginx-1.23.2\conf\nginx.conf
:: clone github repository
git clone https://github.com/pudo58/application
:: install curl if not installed
:: install nginx server
curl  http://nginx.org/download/nginx-1.23.2.zip --output nginx.zip
:: install nodejs if not installed
node -v
if %errorlevel% == 0 (
    echo "Nodejs is installed"
) else (
    curl -i -x GET https://nodejs.org/dist/v16.13.0/node-v16.13.0-x64.msi -output node.msi
    msiexec /i node.msi /qn
)
:: unzip nginx
tar -xf nginx.zip
:: install nodejs modules
del /Q /S %path_nginx_server%\*
:: delete nginx zip file
del /Q /S %path_application%\nginx.zip
:: move files to nginx server
xcopy /E /I /Y %path_application%\application\frontend\* %path_nginx_server%
:: move nginx.conf to nginx server
copy %path_file_github%\nginx.conf %path_nginx_config%
:: start nginx server
cd %path_application%\nginx-1.23.2 && start nginx.exe
echo "nginx started"
:: open chrome port 80
start chrome http://localhost:80

java -jar %path_application%\application\backend\app\deploy\app-0.0.1-SNAPSHOT.jar


