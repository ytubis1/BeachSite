<html>
 <head>
  <title>PHP MySqli interface to Beach site</title>
 </head>
 <body>
 <?php echo '<p>Trting to connect to the data base</p>';
        $mysqli = new mysqli('127.0.0.1', 'your_user', 'your_pass', 'sakila');
        if ($mysqli->connect_errno) {
                echo '<p>Data base could not be reached!!!!</p>';
                echo "Errno: " . $mysqli->connect_errno . "\n"; ?>
                // TODO -- show something nice
                exit;
        }

        $sql = "SELECT actor_id, first_name, last_name FROM actor WHERE actor_id = $aid";
        if (!$result = $mysqli->query($sql)) {
                echo "Sorry, the website is experiencing problems.";
                // TODO -- show something nice
                exit;
        }

        // check if data exists
        if ($result->num_rows === 0) {
                echo "Return error according to the sql query";
                exit;
        }

 </body>
</html>
