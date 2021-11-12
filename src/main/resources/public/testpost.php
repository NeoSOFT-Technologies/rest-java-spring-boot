<?php
	if(isset($_POST['username']) && isset($_POST['password']) && 
				isset($_POST['version']) && isset($_POST['email']) && isset($_POST['purchase']) ) {
		
		echo "Hello, " . $_POST['username'] . "\n Fantastic! You know --: " . $_POST['language'] . " :--\n
			Your encoded password is: " . base64_encode($_POST['password']);
			
	}
?>