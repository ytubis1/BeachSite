
<?php
header('Access-Control-Allow-Origin: *'); 

	include 'surfer.php';
	include 'yoga.php';
        include 'party.php';
	include 'family.php';

	if ( $_GET['person_type'] == "surfer") {
		fetchSurferData();		
	}
	
	else if ( $_GET['person_type'] == "yoga") {
		fetchYogaData();		
	}

        else if ( $_GET['person_type'] == "party") {
                fetchPartyData();
        }

	else if ( $_GET['person_type'] == "family") {
                fetchFamilyData();
        }
