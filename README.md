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

# Glance on array
Zhous-MacBook-Pro-2:src zhouhang$ java NDArray
array([[[[8279, 3129, 8176, 6261, 4622]
         [7222, 9217, 869, 2323, 2876]
         [8861, 578, 7734, 2491, 2338]
         [6589, 8497, 4068, 4660, 6242]]

        [[1833, 5117, 5760, 3465, 4974]
         [2784, 7194, 3516, 7293, 1424]
         [1379, 352, 4404, 5145, 8775]
         [9826, 437, 6322, 3282, 3791]]

        [[9332, 3682, 707, 2415, 4794]
         [1516, 6600, 8144, 9212, 793]
         [8857, 8619, 7894, 9345, 6689]
         [1652, 2977, 6277, 6342, 3507]]]


       [[[9716, 5388, 5626, 1754, 9814]
         [8663, 7834, 3411, 1041, 4915]
         [4915, 8888, 9778, 3435, 3557]
         [7518, 2973, 3192, 6416, 7998]]

        [[5086, 4502, 8715, 5313, 9962]
         [8339, 6803, 9432, 4132, 6359]
         [2416, 3686, 4187, 8235, 4564]
         [8074, 1777, 2743, 668, 7740]]

        [[5190, 3756, 9529, 515, 5715]
         [6251, 9912, 8177, 1138, 9536]
         [8829, 1180, 7786, 382, 4159]
         [266, 1673, 2728, 8748, 3656]]]],  dtype=NumJ.Int32)