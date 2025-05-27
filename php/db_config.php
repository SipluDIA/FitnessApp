<?php
// File: db_config.php
// Database configuration
$host = 'localhost';
$db_name = 'fitness_app';
$db_user = 'root';
$db_pass = '';

// Set up PDO
try {
    $pdo = new PDO("mysql:host=$host;dbname=$db_name;charset=utf8mb4", $db_user, $db_pass);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(["success" => false, "message" => "Database connection failed: " . $e->getMessage()]);
    exit;
}

// CORS headers (allow your Android app domain or *)
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json; charset=UTF-8');

?>