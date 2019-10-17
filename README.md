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
```
Zhous-MacBook-Pro-2:src zhouhang$ java Utils
array([[[[1028, 3219, 3331, 2402, 7473]
         [4975, 1954, 83, 4979, 4687]
         [9955, 8907, 7976, 3183, 323]
         [3721, 9318, 5868, 4799, 3841]]

        [[9843, 4467, 4496, 639, 2740]
         [6355, 3201, 292, 3760, 7620]
         [4558, 2895, 1488, 3121, 7019]
         [2968, 3638, 2145, 494, 9253]]

        [[8455, 2804, 8490, 1919, 6711]
         [6233, 7512, 7631, 6927, 9004]
         [7108, 6626, 7453, 3851, 3042]
         [7804, 4824, 8826, 6908, 2632]]]


       [[[3869, 4557, 9177, 9501, 5052]
         [4579, 8651, 5069, 6985, 2194]
         [6404, 119, 223, 8061, 1942]
         [7468, 9511, 354, 9894, 1311]]

        [[7201, 751, 940, 4581, 4269]
         [1525, 6714, 1803, 8585, 8837]
         [7111, 87, 7895, 6039, 7878]
         [8247, 1145, 9114, 8072, 6550]]

        [[4588, 7062, 6250, 445, 7854]
         [5420, 3836, 7766, 398, 3311]
         [8832, 1851, 7231, 652, 1899]
         [2813, 1544, 4740, 8603, 6529]]]],    dtype=NumJ.Int32)
         ```