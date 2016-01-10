FROM stilliard/pure-ftpd
MAINTAINER Carlos Badenes <cbadenes@gmail.com>

RUN apt-get install -y expect
ADD create_user.exp /

RUN expect -f /create_user.exp
RUN pure-pw mkdb