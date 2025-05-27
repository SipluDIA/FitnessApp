<?php
// File: login.php
require 'db_config.php';

$input = json_decode(file_get_contents('php://input'), true);
$username = trim($input['username'] ?? '');
$password = $input['password'] ?? '';

if (!$username || !$password) {
    echo json_encode(["success" => false, "message" => "Username and password are required."]);
    exit;
}

// Fetch user
$stmt = $pdo->prepare("SELECT id, username, password FROM users WHERE username = :username");
$stmt->execute([':username' => $username]);
$user = $stmt->fetch(PDO::FETCH_ASSOC);

if (!$user) {
    echo json_encode(["success" => false, "message" => "Invalid username or password."]);
    exit;
}

// Verify password
if (password_verify($password, $user['password'])) {
    // Optionally generate a session or JWT here
    echo json_encode([
    "success" => true,
    "userId" => $user['id'],
    "userName" => $user['username'],
    "message" => "Login successful."
]);
} else {
    echo json_encode(["success" => false, "message" => "Invalid username or password."]);
}
?>