const admin = require('firebase-admin');
const express = require('express');
const jwt = require('jsonwebtoken');
const bodyParser = require('body-parser');
const bcrypt = require('bcrypt');


const app = express();
const port = process.env.PORT || 3000;
const secretKey = 'no more waste';

// Firebase Admin SDK
const serviceAccount = require('./serviceaccountkeyfirebase.json'); // Path to your Firebase Admin SDK key
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

app.use(bodyParser.json());

app.post('/login', async (req, res) => {
    const { username, password } = req.body;
    try {
        // Firestore query based on username
        const userDoc = await admin.firestore().collection('user').doc(username).get();

        // Check if the user exists and compare hashed passwords
         if (userDoc.exists) {
        const hashedPassword = userDoc.data().password;
  
        // Compare hashed passwords using bcrypt
        const passwordsMatch = await bcrypt.compare(password, hashedPassword);
         
        // Check if the user exists and the password is correct
        if (passwordsMatch) {
          const token = jwt.sign({ username }, secretKey, { expiresIn: '1h' });
          res.json({ token, error: false});
        } else {
          res.status(401).json({ token: '', error: true});
        }} 
        }catch (error) {
        console.error(error);
        res.status(500).json({ error: true, token: '' });
      }
    });

    app.post('/register', async (req, res) => {
        const { name, username, password } = req.body;
        
        // Check if the username already exists
        const userExists = await checkIfUsernameExists(username);
        
        if (userExists) {
            return res.status(400).json({ error: true });
        }
        
        // Hash the password before storing it
        const hashedPassword = await bcrypt.hash(password, 10);
        
        // Save the username and hashed password to Firestore
        await saveUser(name, username, hashedPassword);
        
        res.json({ Error: false });
        });
  
async function checkIfUsernameExists(username) {
    const userDoc = await admin.firestore().collection('user').doc(username).get();
    return userDoc.exists;
    }
      
async function saveUser(name, username, hashedPassword) {
    await admin.firestore().collection('user').doc(username).set({name:name, password: hashedPassword, created_at: new Date() });
    }

app.listen(port, () => {
  console.log(`Server is running on http://0.0.0.0:${port}`);
});

const verifyToken = (req, res, next) => {
    const token = req.headers.authorization;
  
    if (!token) {
      return res.status(403).json({ error: 'No token provided'});
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