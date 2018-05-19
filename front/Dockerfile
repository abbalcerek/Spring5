FROM node:9.11.1

ADD ./package.json ./webpack.mix.js /app/

WORKDIR /app/

RUN npm install

ADD ./resources /app/resources

CMD npm run watch

#CMD /bin/bash