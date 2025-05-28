<?php
// File: read_profile.php
require 'db_config.php';

header('Content-Type: application/json');

$input = json_decode(file_get_contents('php://input'), true);
$userId = intval($input['userId'] ?? 0);

if (!$userId) {
    echo json_encode(["success" => false, "message" => "User ID is required."]);
    exit;
}

try {
    $stmt = $pdo->prepare("SELECT id, username, gender, age, weight, height FROM users WHERE id = :userId");
    $stmt->execute([':userId' => $userId]);
    $user = $stmt->fetch(PDO::FETCH_ASSOC);
    if ($user) {
        echo json_encode([
            "success" => true,
            "user" => $user
        ]);
    } else {
        echo json_encode(["success" => false, "message" => "User not found."]);
    }
} catch (PDOException $e) {
    echo json_encode(["success" => false, "message" => "Error: " . $e->getMessage()]);
}
?>
