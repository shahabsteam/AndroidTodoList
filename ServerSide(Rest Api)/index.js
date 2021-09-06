var express = require('express');
var jwt = require("jsonwebtoken");
var mysql = require('mysql');
const bp = require('body-parser');
const app = express();
app.use(bp.json())
app.use(bp.urlencoded({ extended: true }))
var connection = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: '',
  database: 'notes',
  timezone: "Iran/Tehran"
})
connection.connect();

app.post('/api',function(req,res){
    console.log(req.body);
    res.json({
        text:'my anwser',
        params: req.body
    })
})
app.post('/api/login',function(req,res){
    
    const username = req.query.username
    const password = req.query.password
   
    console.log(password,username)
    connection.query(`SELECT * FROM x_user where username='${username}' AND password ='${password}' `, function (err, rows, fields) {
        if(rows.length>0){
            const user = { id : rows[0].user_id ,  username : username}
            console.log(user);
            const token= jwt.sign({user},"my_secret_key");
            res.json({
                token : token
            })
        }else{
            res.sendStatus(404);
        }
        if (err) throw err
      })
})
app.post('/api/register',function(req,res){
    
    const username = req.query.username;
    const password = req.query.password;
    const email = req.query.email;
    connection.query(`SELECT * FROM x_user where username ="${username}" `,function(err,rows,result){
        if(rows.length>0){
            res.json({
                status : false ,
                message : "user duplicate login now"
            })
            
        }else{
            connection.query(`INSERT INTO x_user (username , password , email) VALUES ("${username}","${password}","${email}") `, function (err, rows, fields) {
                console.log(rows);
                
                if(rows.warningCount==0){
                    res.json({
                     status : true,
                     message : "your account created \n now you can login"
                    })
                }else{
                 res.json({
                     status : false,
                     message : "something went wrong try again"
                    })
                }
                 if (err) throw err
               })
       

        }
    } );
    
        
     
    
    
})
app.get('/api/protected',ensureToken,function(req,res){
    console.log(req.token);
    jwt.verify(req.token,"my_secret_key", function(err , data ){
        if(err){
            res.sendStatus(403);
        }else{
            res.json({
                text : "this is protected",
                data:data
            })
            
        }
    })
    
})
app.get('/api/getnotes',ensureToken,function(req,res){
    
    jwt.verify(req.token,"my_secret_key", function(err , data ){
        if(err){
            console.log("No Authorization ")
            res.sendStatus(403);
        }else{
            connection.query(`select * from x_note where user_id=${data.user.id} `, function (err, rows, fields) {
                console.log(rows);
                console.log(data.user.id);
            res.json(
                rows
            )
            
        })
    }
})
    
})
//delete a note from database 
app.delete('/api/deletenote',ensureToken,function(req,res){
    const note_id = req.query.id;
    jwt.verify(req.token,"my_secret_key", function(err , data ){
        if(err){
            res.sendStatus(403);
        }else{
            connection.query(`delete  from x_note where note_id=${note_id} `, function (err, rows, fields) {
                console.log(rows);
                console.log(data.user.id);
            res.json(
                rows
            )
            
        })
    }
})
    
})
//update an existing note
app.put('/api/updatenote',ensureToken,function(req,res){
    const note_id = req.query.id;
    const isDone = req.query.isDone;
    const eventTime =req.query.eventTime;
    const title = req.query.title;
    const description = req.query.description;
    console.log(note_id);
    jwt.verify(req.token,"my_secret_key", function(err , data ){
        if(err){
            res.sendStatus(403);
        }else{
            connection.query(`UPDATE x_note SET title="${title}",description="${description}",isDone="${isDone}",eventTime="${eventTime}"
             where note_id=${note_id} `, function (err, rows, fields) {
                 if(err)
                    throw err;
                console.log(rows);
                console.log(data.user.id);
            res.json(
                rows
            )
            
        })
    }
})
    
})
//save a new note
app.put('/api/addnote',ensureToken,function(req,res){
    const note_id = req.query.id;
    const isDone = req.query.isDone;
    const eventTime =req.query.eventTime;
    const title = req.query.title;
    const description = req.query.description;
    jwt.verify(req.token,"my_secret_key", function(err , data ){
        if(err){
            res.sendStatus(403);
        }else{
            connection.query(`INSERT INTO x_note  (title,description,isDone,eventTime,user_id)
             VALUES ("${title}","${description}","${isDone}","${eventTime}","${data.user.id}")
              `, function (err, rows, fields) {
                  if(err)
                    res.sendStatus(403);
                console.log(rows);
                console.log(data.user.id);
            res.json(
                rows
            )
            
        })
    }
})
    
})
app.put('/api/isdone',ensureToken,function(req,res){
    const note_id = req.query.id;
    const isDone = req.query.isDone;
    jwt.verify(req.token,"my_secret_key", function(err , data ){
        if(err){
            res.sendStatus(403);
        }else{
            connection.query(`UPDATE x_note SET isDone="${isDone}"where note_id=${note_id} `)
              , function (err, rows, fields) {
                  if(err)
                    res.sendStatus(403);
                console.log(rows);
                console.log(data.user.id);
            res.json(
                rows
            )
            
        }
    }
})
})
    


app.get('/query',function(req,res){
    res.json({
        status : ok
    })

})
function ensureToken(req,res,next){
    const bearerHeader = req.headers['authorization'];
    if(typeof bearerHeader!=="undefined"){
        const bearer = bearerHeader.split(" ")
        const bearerToken  = bearer[1];
        req.token = bearerToken;
        next();
    }else{
        res.sendStatus(403);
    }
}
function query(sql){
    connection.connect();
    connection.query(sql, function (err, rows, fields) {
        if(rows.length>0){
                res.json(rows)
        }
        if (err) throw err
      })
      connection.end()
}
 function checkduplicate(err,rows,fields){
        if(rows.length>0){
            return false;
        }  
        else {
          return true;
        } 
}
app.listen(3000,fun=>{
    console.log('Hey there listening on 3000')

    });

