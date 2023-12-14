from flask import Flask, request, jsonify
import tensorflow as tf
import numpy as np
from datetime import datetime
import os
import io
import firebase_admin
from firebase_admin import credentials
from google.cloud import storage, firestore

#connecting firebase
cred = credentials.Certificate('serviceaccountkeyfirebase.json')
firebase_admin.initialize_app(cred)
os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = "serviceaccountkeyfirebase.json"


storage_client = storage.Client()
db = firestore.Client()


keras_load_img = tf.keras.utils.load_img
img_to_array = tf.keras.utils.img_to_array


model_path = 'model.tflite'
IMAGE_SIZE = (224, 224)    

app = Flask(__name__)


interpreter = tf.lite.Interpreter(model_path=model_path)
interpreter.allocate_tensors()


input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()


def get_result(image_data):
    # Load the image
    orig_img = keras_load_img(io.BytesIO(image_data), target_size=IMAGE_SIZE)

    # Resize and normalize the image
    image = img_to_array(orig_img)
    image /= 255
    image = np.expand_dims(image, axis=0)

    # Perform inference
    interpreter.set_tensor(input_details[0]['index'], image)
    interpreter.invoke()

    # Get output then return it
    output = interpreter.get_tensor(output_details[0]['index'])

    if output > 0.5:
         return "Sampah Anorganik"
    else:
         return "Sampah Organik"
    

def save_image_to_cloud_storage(username, image_data):
    # Generate a unique filename based on user ID and timestamp
    timestamp = datetime.now().strftime('%d_%m_%YT%H_%M_%S')
    filename = f"{username}_{timestamp}.jpg"
    print(filename)
    
    # Get the Cloud Storage bucket
    bucket = storage_client.bucket('iwasteapp')

    # Create a blob (object) in the bucket
    blob = bucket.blob(filename)

    # Upload the image data to Cloud Storage
    blob.upload_from_file(io.BytesIO(image_data), content_type="image/jpeg")
    # blob.upload_from_string(image_data, content_type='image/jpeg')

    # Get the public URL of the uploaded image
    image_url = blob.public_url

    return image_url


def save_image_information_to_firestore(username, image_url, result):
    # Create a new document in the 'images' collectio
    image_ref = db.collection('images').add({
        'username': username,
        'image_url': image_url,
        'timestamp': firestore.SERVER_TIMESTAMP,
        'type': result
    })
    return image_ref


@app.route("/")
def index():
    return('hello world')

@app.route('/predict', methods=['POST'])
def predict():
        # Get the image data from the request
        image_data = request.files['image'].read()

        #get username
        username = request.form['username']

        #Run inference
        result = get_result(image_data)
        print("Inference Result:", result)

        #save image to cloud storage    
        image_url= save_image_to_cloud_storage(username, image_data)

        #save image data to firestore
        save_image_information_to_firestore(username, image_url, result)

        # Return the prediction result
        return jsonify({'result': result})
    
@app.route('/history', methods=['POST'])
def history():
    try:
        #get username 
        username = request.form['username']

        # Create a query to get documents where 'username' equals the requested username
        query = db.collection('images').where('username', '==', username)

        # Execute the query
        results = query.stream()

        # Create a list to store the results
        images = []

        # Iterate over the query results
        for result in results:
            # Convert the document to a dictionary
            image_data = result.to_dict()
            
            # Append the dictionary to the list
            images.append(image_data)

        # Return the list of images in the response
        return jsonify({'images': images})

    except Exception as e:
        return jsonify({'error': str(e)}), 500


if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=int(os.environ.get('PORT', 8080)))
