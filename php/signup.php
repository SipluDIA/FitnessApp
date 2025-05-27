<?php
// File: signup.php
require 'db_config.php';

// Read JSON POST body
$input = json_decode(file_get_contents('php://input'), true);
$username = trim($input['username'] ?? '');
$email    = trim($input['email'] ?? '');
$password = $input['password'] ?? '';

if (!$username || !$email || !$password) {
    echo json_encode(["success" => false, "message" => "All fields are required."]);
    exit;
}

// Validate email
if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    echo json_encode(["success" => false, "message" => "Invalid email format."]);
    exit;
}

// Hash password
$hash = password_hash($password, PASSWORD_BCRYPT);

// Insert user
try {
    $stmt = $pdo->prepare("INSERT INTO users (username, email, password) VALUES (:username, :email, :password)");
    $stmt->execute([
        ':username' => $username,
        ':email' => $email,
        ':password' => $hash
    ]);

    echo json_encode(["success" => true, "message" => "User registered successfully."]);
} catch (PDOException $e) {
    // Duplicate entry?
    if ($e->errorInfo[1] == 1062) {
        echo json_encode(["success" => false, "message" => "Username or email already exists."]);
    } else {
        echo json_encode(["success" => false, "message" => "Registration failed: " . $e->getMessage()]);
    }
}
?>