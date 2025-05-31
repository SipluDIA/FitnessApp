<?php
// File: upload_profile_image.php
require 'db_config.php';

header('Content-Type: application/json');

$userId = $_POST['userId'] ?? 0;
if (!$userId || !isset($_FILES['profile_image'])) {
    echo json_encode(['success' => false, 'message' => 'User ID and image required.']);
    exit;
}

$targetDir = 'uploads/';
if (!is_dir($targetDir)) mkdir($targetDir, 0777, true);

$filename = uniqid() . '_' . basename($_FILES['profile_image']['name']);
$targetFile = $targetDir . $filename;

if (move_uploaded_file($_FILES['profile_image']['tmp_name'], $targetFile)) {
    // Add your BASE_URL here, e.g., https://yourdomain.com/
    $baseUrl = 'https://dreamarray.com/api/';
    $imageUrl = $baseUrl . $targetFile;
    // Update user profile_image_url in DB
    $stmt = $pdo->prepare('UPDATE users SET profile_image_url = :url WHERE id = :userId');
    $stmt->execute([':url' => $imageUrl, ':userId' => $userId]);
    echo json_encode(['success' => true, 'imageUrl' => $imageUrl]);
} else {
    echo json_encode(['success' => false, 'message' => 'Image upload failed.']);
}
?>
