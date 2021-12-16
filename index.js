const express = require('express');
const { graphqlHTTP } = require('express-graphql');
const port = 8888; // номер порта
const schema=require('./schema/schema');
const app = express(); // инициализация объекта приложения
//выполнение серий функций req,res при совпадении корневого пути
app. use(
    '/librarium',
    graphqlHTTP({
        schema: schema,
        graphiql: true,
    })
)
app.listen(port);
