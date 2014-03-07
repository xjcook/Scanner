<?php

// Prepare database.
$db = new PDO('sqlite:db.sqlite');
$db->exec("CREATE TABLE IF NOT EXISTS barcodes (
            id INTEGER PRIMARY KEY, 
            firstname TEXT, 
            lastname TEXT, 
            address TEXT, 
            barcode TEXT NOT NULL)");
$db->exec("CREATE UNIQUE INDEX IF NOT EXISTS 
            barcode_idx ON barcodes (barcode)");

if (isset($_GET['q'])) {
    $barCode = $_GET['q'];
    $query = "SELECT firstname, lastname, address 
                FROM barcodes WHERE barcode = :barcode LIMIT 1";
    $stmt = $db->prepare($query);
    $stmt->bindParam(':barcode', $barCode);
    $stmt->execute();
    $row = $stmt->fetch();

    // Check if barcode exists in DB.
    if (!empty($row)) {
        // Build XML.
        $domDoc = new DOMDocument;
        $scanElement = $domDoc->createElement('scan');
        $scanNode = $domDoc->appendChild($scanElement);
        
        /* Firstname */
        $detailElement = $domDoc->createElement('detail');
            $attr = $domDoc->createAttribute('id');
            $attrVal = $domDoc->createTextNode('firstname');
            $attr->appendChild($attrVal);
            $detailElement->appendChild($attr);
        $detailNode = $scanNode->appendChild($detailElement);
        
        $textNode = $domDoc->createTextNode($row['firstname']);
        $detailNode->appendChild($textNode);
        
        /* Lastname */
        $detailElement = $domDoc->createElement('detail');
            $attr = $domDoc->createAttribute('id');
            $attrVal = $domDoc->createTextNode('lastname');
            $attr->appendChild($attrVal);
            $detailElement->appendChild($attr);
        $detailNode = $scanNode->appendChild($detailElement);
        
        $textNode = $domDoc->createTextNode($row['lastname']);
        $detailNode->appendChild($textNode);
        
        /* Address */
        $detailElement = $domDoc->createElement('detail');
            $attr = $domDoc->createAttribute('id');
            $attrVal = $domDoc->createTextNode('address');
            $attr->appendChild($attrVal);
            $detailElement->appendChild($attr);
        $detailNode = $scanNode->appendChild($detailElement);
        
        $textNode = $domDoc->createTextNode($row['address']);
        $detailNode->appendChild($textNode);
        
        header('Content-Type: application/xml');
        echo $domDoc->saveXML();
    } else {
        // Get POST data.
        $rawData = file_get_contents('php://input');

        if (!empty($rawData)) {
            $domDoc = DOMDocument::loadXML($rawData);

            if ($domDoc->validate()) {
                $detailElement = $domDoc->getElementById('firstname');
                $firstName = $detailElement->nodeValue;
                
                $detailElement = $domDoc->getElementById('lastname');
                $lastName = $detailElement->nodeValue;
                
                $detailElement = $domDoc->getElementById('address');
                $address = $detailElement->nodeValue;

                $insert = "INSERT INTO barcodes (firstname, lastname, address, barcode)
                            VALUES (:firstname, :lastname, :address, :barcode)";
                $stmt = $db->prepare($insert);
                $stmt->bindParam(':firstname', $firstName);
                $stmt->bindParam(':lastname', $lastName);
                $stmt->bindParam(':address', $address);
                $stmt->bindParam(':barcode', $barCode);

                if (!$stmt->execute()) {
                    http_response_code(500);
                    error_log(print_r($stmt->errorInfo(), true));
                    error_log("Insert failed.");
                }
            } else {
                http_response_code(400);
                error_log("Document is not valid.");
            }
        }
    }
} else {
    http_response_code(400);
    error_log("Barcode is required.");
}

?>
