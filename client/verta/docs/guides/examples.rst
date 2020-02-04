Integrations and Examples
=========================

Framework Integrations
----------------------
Out of the box, Verta provides integrations to capture items from popular machine learning
frameworks. For example usage, see `the documentation page <../reference/api/integrations.html>`__.

====================  ===========================================
Framework             Logged Metadata
====================  ===========================================
Keras                 network topology, training hyperparameters,
                      fitting metrics
PyTorch               network topology
scikit-learn          model hyperparameters
TensorFlow 1.X & 2.X  TensorBoard metrics
XGBoost               evaluation metrics
====================  ===========================================

Example Notebooks
-----------------
`Verta's GitHub repository <https://github.com/VertaAI/modeldb-oss>`_ houses `various example
projects <https://github.com/VertaAI/modeldb-oss/tree/master/client/workflows>`_ for reference and
exploration.

Annoy
^^^^^
- `Nearest Neighbor Search with TensorFlow, GloVe Embeddings, and Verta Class Model Deployment
  <https://github.com/VertaAI/modeldb-oss/blob/master/client/workflows/demos/Nearest-Neighbors-TF-Glove.ipynb>`__
- `Nearest Neighbor Search with TensorFlow Hub and Verta Class Model Deployment
  <https://github.com/VertaAI/modeldb-oss/blob/master/client/workflows/demos/Embedding-and-Lookup-TF-Hub.ipynb>`__

Keras
^^^^^
- `Fully-Connected Neural Network
  <https://github.com/VertaAI/modeldb-oss/blob/master/client/workflows/examples/tensorflow.ipynb>`__
- `RNN Text Classification
  <https://github.com/VertaAI/modeldb-oss/blob/master/client/workflows/examples/text_classification_rnn.ipynb>`__
- `Text Classification with TensorFlow Hub
  <https://github.com/VertaAI/modeldb-oss/blob/master/client/workflows/examples/basic_text_classification_with_tfhub.ipynb>`__
- `Tokenizer Training and Text Classification
  <https://github.com/VertaAI/modeldb-oss/blob/master/client/workflows/demos/tf-text-classification.ipynb>`__

NLTK
^^^^
- `Part-of-Speech Tagging
  <https://github.com/VertaAI/modeldb-oss/blob/master/client/workflows/demos/setup-script.ipynb>`__
- `Spam Detection with scikit-learn and Verta Class Model Deployment
  <https://github.com/VertaAI/modeldb-oss/blob/master/client/workflows/demos/Spam-Detection.ipynb>`__

PyTorch
^^^^^^^
- `Fully-Connected Neural Network
  <https://github.com/VertaAI/modeldb-oss/blob/master/client/workflows/examples/pytorch.ipynb>`__

scikit-learn
^^^^^^^^^^^^
- `Logistic Regression with Grid Search
  <https://github.com/VertaAI/modeldb-oss/blob/master/client/workflows/examples/sklearn.ipynb>`__
- `Logistic Regression with Verta Deployment
  <https://github.com/VertaAI/modeldb-oss/blob/master/client/workflows/demos/census-end-to-end.ipynb>`__
- `Logistic Regression with Verta Deployment and S3 Data Versioning
  <https://github.com/VertaAI/modeldb-oss/blob/master/client/workflows/demos/census-end-to-end-s3-example.ipynb>`__
- `Logistic Regression with Verta Deployment and Local Data Versioning
  <https://github.com/VertaAI/modeldb-oss/blob/master/client/workflows/demos/census-end-to-end-local-data-example.ipynb>`__
- `Spam Detection with NLTK and Verta Class Model Deployment
  <https://github.com/VertaAI/modeldb-oss/blob/master/client/workflows/demos/Spam-Detection.ipynb>`__

SpaCy
^^^^^
- `CNN Text Classification
  <https://github.com/VertaAI/modeldb-oss/blob/master/client/workflows/examples/text_classification_spacy.ipynb>`__

TensorFlow
^^^^^^^^^^
- `Nearest Neighbor Search with Annoy, GloVe Embeddings, and Verta Class Model Deployment
  <https://github.com/VertaAI/modeldb-oss/blob/master/client/workflows/demos/Nearest-Neighbors-TF-Glove.ipynb>`__
- `Nearest Neighbor Search with TensorFlow Hub and Verta Class Model Deployment
  <https://github.com/VertaAI/modeldb-oss/blob/master/client/workflows/demos/Embedding-and-Lookup-TF-Hub.ipynb>`__

XGBoost
^^^^^^^
- `Random Forest with Grid Search
  <https://github.com/VertaAI/modeldb-oss/blob/master/client/workflows/examples/xgboost.ipynb>`__
