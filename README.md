# Janet
Light weight and useless neural network framework implemented in Java (JavaNet->Janet)

key Point:
understanding optimizer in the framework
understanding backpropagation

the final api looks like this:

NumJ X = ... // either randomly generated or converted from primitive array
NumJ Y = ... //

NN nn = new NN(); // initialize the weights, a regular dense network arch by default
Layer h1 = new Layer(input_dim = 100, output_dim = 50, activation = "Relu");
Layer h2 = new Layer(input_dim = 50, output_dim = 10, activation = "Relu");
nn.add_layer(h1);
nn.add_layer(h2);

// or just shortcut, like
// nn.add_layer(input_dim = 100, output_dim = 50, activation = "Relu");
// as you wish

nn.set_optimizer("adam"); // not necessarily adam of course, in fact, if only gonna implement one optimizer, we can save this one as default
nn.set_loss("MSE") //

nn.train(X, Y, learning_rate = 0.1, iteration = 100, verbose = "loss"); // iteration,learning_rate as training parameter, you can extend batch_size etc. if you like; verbose is for representing the training process

NumJ result = nn.predict(x);


* only train on dataset one by one, not gonna work on the epcho thang, so the input will only be 2-d
* only support training on given iterations.