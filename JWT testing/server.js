// server.js

const express = require('express');
const jwt = require('jsonwebtoken');
const bodyParser = require('body-parser');

const app = express();
const port = process.env.PORT || 3000;
const secretKey = 'no more waste';

app.use(bodyParser.json());

app.post('/login', (req, res) => {
    const { username, password } = req.body;
  
    // Hardcoded example credentials
    const validUsername = 'admin';
    const validPassword = 'admin';
  
    // Perform authentication logic
    if (username === validUsername && password === validPassword) {
      const token = jwt.sign({ username }, secretKey, { expiresIn: '1h' });
      res.json({ token });
    } else {
      res.status(401).json({ error: 'Wrong username or password' });
    }
  });
  

app.listen(port, () => {
  console.log(`Server is running on http://0.0.0.0:${port}`);
});

const verifyToken = (req, res, next) => {
    const token = req.headers.authorization;
  
    if (!token) {
      return res.status(403).json({ error: 'No token provided' });
    }
  
    jwt.verify(token, secretKey, (err, decoded) => {
      if (err) {
        return res.status(401).json({ error: 'Failed to authenticate token' });
      }
      req.user = decoded;
      next();
    });
  };
  
  app.get('/protected', verifyToken, (req, res) => {
    res.json({ message: 'This is a protected route' });
  });
  
  app.set("trust proxy", true)