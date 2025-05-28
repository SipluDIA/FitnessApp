<?php
// File: update_profile.php
require 'db_config.php';

// Read JSON POST body
$input = json_decode(file_get_contents('php://input'), true);
$userId = intval($input['userId'] ?? 0);
$name = trim($input['name'] ?? '');
$gender = trim($input['gender'] ?? '');
$age = intval($input['age'] ?? 0);
$weight = floatval($input['weight'] ?? 0);
$height = floatval($input['height'] ?? 0);

if (!$userId || !$name) {
    echo json_encode(["success" => false, "message" => "User ID and name are required."]);
    exit;
}

try {
    $stmt = $pdo->prepare("UPDATE users SET username = :name, gender = :gender, age = :age, weight = :weight, height = :height WHERE id = :userId");
    $stmt->execute([
        ':name' => $name,
        ':gender' => $gender,
        ':age' => $age,
        ':weight' => $weight,
        ':height' => $height,
        ':userId' => $userId
    ]);
    echo json_encode(["success" => true, "message" => "Profile updated successfully."]);
} catch (PDOException $e) {
    echo json_encode(["success" => false, "message" => "Update failed: " . $e->getMessage()]);
}
?>
