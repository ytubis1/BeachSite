 <?php

	function fetchYogaData() {

		$mysqli = new mysqli('127.0.0.1', 'root', 'hck17ba', 'beach_app');
		if ($mysqli->connect_errno) {
			echo '<p>Data base could not be reached!!!!</p>';
			echo "Errno: " . $mysqli->connect_errno . "\n";
			// TODO -- show something nice
			exit;
		}

		$sql = "SELECT * FROM `sea_information` WHERE `isolated` = 1 AND `heat_index_id` < 4 AND `sunlight_level` < 6";
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

		$arrayData = array();

		while( $rawData = $result->fetch_assoc()) {
			array_push($arrayData, $rawData);
		}

		echo json_encode($arrayData);

		$result->free();
		$mysqli->close();

	}

	?>
