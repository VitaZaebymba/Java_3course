const graphql=require('graphql');
const _ = require('lodash');
const{
    GraphQLID,
    GraphQLString,
    GraphQLList,
    GraphQLInt,
    GraphQLNonNull,
    GraphQLObjectType,
    GraphQLSchema
}=graphql;
const books = [
    { id: "1", name: "Зов Ктулху", author:"1", year: "1928", genre: "Ужасы"},
    { id: "2", name: "Хребты безумия", author:"1", year: "1936", genre: "Ужасы"},
    { id: "3", name: "Тень над Иннсмутом", author:"1", year: "1936", genre: "Ужасы"},
    { id: "4", name: "Ужас Данвича", author:"1", year: "1929", genre: "Ужасы"},
    { id: "5", name: "Кошки Ултара", author:"1", year: "1920", genre: "Ужасы"},
    { id: "6", name: "Оно", author:"2", year: "1986", genre: "Ужасы"},
    { id: "7", name: "Сияние", author:"2", year: "1977", genre: "Ужасы"},
    { id: "8", name: "Противостояние", author:"2", year: "1978", genre: "Ужасы"},
    { id: "9", name: "Керри", author:"2", year: "1974", genre: "Ужасы"},
    { id: "10", name: "Зелёная миля", author:"2", year: "1996", genre: "Ужасы"},
]

const authors = [
    {id: "1",first_name: "Говард",last_name: "Лавкрафт"},
    {id: "2",first_name: "Стивен", last_name: "Кинг"}
]


const BookType = new GraphQLObjectType({
    name: "Book",
    fields: ()=>({
        id: {type: GraphQLID},
        name: {type: GraphQLString},
        author: {type: AuthorType,
            resolve(parent, args){
                return _.find(authors,{id:parent.author});
            }
        },
        year:{type: GraphQLString},
        genre:{type: GraphQLString}
    })
});
const AuthorType = new GraphQLObjectType({
    name:"Author",
    fields:()=>({
        id: {type: GraphQLID},
        first_name: {type: GraphQLString},
        last_name: {type: GraphQLString},
        book: {
            type : new GraphQLList(BookType),
            resolve(parent, args){
                return _.filter(books,{author:parent.id});
            }
        }
    })
});
const RootQuery = new GraphQLObjectType({
    name: 'RootQueryType',
    fields:{
        info:{
            type:GraphQLString,
            resolve(parent, args){
                return "Сервер запущен"
            }
        },
        book:{
            type: BookType,
            args: {id:{type: GraphQLID}},
            resolve(parent, args){
                return _.find(books, {id: args.id});
            }
        },
        books:{
            type: new GraphQLList(BookType),
            resolve(parent, args){
                return books;
            }
        },
        authors:{
            type: new GraphQLList(AuthorType),
            resolve(parent, args){
                return authors;
            }
        },
        author:{
            type: AuthorType,
            args: {id: {type: GraphQLID}},
            resolve(parent, args){
                return _.find(authors, {id: args.id});
            }
        }
    }
});
module.exports = new GraphQLSchema({
    query: RootQuery
});