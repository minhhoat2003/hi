const express = require('express');
const bodyParser = require('body-parser');
const mysql = require('mysql2');
const path = require('path');

//emulator.exe -avd pixel_9.0 -gpu host -dns-server 8.8.8.8

const app = express();
const port = process.env.PORT || 3000;

//Sử dụng body-parser để phân tích dữ liệu JSON
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'datvemaybay'
});

db.connect((err) => {
    if (err) { throw err; }
    console.log('Connected to database');
});

// Thêm vào trước app.listen
app.use('/images', express.static(path.join(__dirname, 'images')));


// API để lấy thông tin người dùng
app.get('/api/user/:iduser', (req, res) => {
    const iduser = parseInt(req.params.iduser);
    const sql = 'SELECT username, fullname, birthdate FROM user WHERE iduser = ?';
    
    db.query(sql, [iduser], (err, results) => {
        if (err) {
            return res.status(500).json({ success: false, message: 'Database error' });
        }

        if (results.length > 0) {
            res.json({ success: true, user: results[0] });
        } else {
            res.json({ success: false, message: 'User not found' });
        }
    });
});

// Route để lấy thông tin khách hàng cho admin
app.get('/customers', (req, res) => {
    const sql = 'SELECT iduser, username, fullname, birthdate FROM user'; // Chọn các trường cần thiết

    db.query(sql, (err, results) => {
        if (err) {
            throw err;
        }

        if (results.length > 0) {
            res.json({ success: true, customers: results });
        } else {
            res.json({ success: false, message: 'No customers found' });
        }
    });
});


// Route để tạo tài khoản mới
app.post('/register', (req, res) => {
    const { username, password, fullname, birthdate } = req.body;
    
    // SQL để kiểm tra xem tên người dùng đã tồn tại chưa
    const checkUserSql = 'SELECT * FROM user WHERE username = ?';
    
    db.query(checkUserSql, [username], (err, results) => {
        if (err) {
            throw err;
        }

        if (results.length > 0) {
            // Tên người dùng đã tồn tại
            return res.json({ success: false, message: 'Username already exists' });
        } else {
            // SQL để thêm người dùng mới
            const insertSql = 'INSERT INTO user (username, password, fullname, birthdate) VALUES (?, ?, ?, ?)';
            db.query(insertSql, [username, password, fullname, birthdate], (err, results) => {
                if (err) {
                    throw err;
                }
                res.json({ success: true, message: 'User registered successfully' });
            });
        }
    });
});


// Route để kiểm tra thông tin đăng nhập
app.post('/login', (req, res) => {
    const { username, password } = req.body;
    const sql = 'SELECT * FROM user WHERE username = ? AND password = ?';
    
    db.query(sql, [username, password], (err, results) => {
        if (err) {
            throw err;
        }
        
        if (results.length > 0) {
            // Login successful, send success and iduser
            const iduser = results[0].iduser;  // Extract iduser from the first result
			const fullname = results[0].fullname;
            res.json({ success: true, iduser, fullname });
        } else {
            // Login failed, no user found
            res.json({ success: false });
        }
    });
});

// Route để kiểm tra thông tin đăng nhập
app.post('/loginadmin', (req, res) => {
    const { username, password } = req.body;
    const sql = 'SELECT * FROM admin WHERE username = ? AND password = ?';
    
    db.query(sql, [username, password], (err, results) => {
        if (err) {
            throw err;
        }
        
        if (results.length > 0) {
            res.json({ success: true });
        } else {
            // Login failed, no user found
            res.json({ success: false });
        }
    });
});

// Route để lấy dữ liệu các chuyến bay
// Route để lấy dữ liệu các chuyến bay theo ngày và loại ghế
app.get('/flights', (req, res) => {
    const { date, classType } = req.query;
    let sql = 'SELECT * FROM flight WHERE DATE(departure_time) = ?';
    let params = [date];

    if (classType === 'economy') {
        sql += ' AND available_economy_seats > 0';
    } else if (classType === 'first_class') {
        sql += ' AND available_first_class_seats > 0';
    }

    db.query(sql, params, (err, results) => {
        if (err) {
            throw err;
        }

        if (results.length > 0) {
            res.json({ success: true, flights: results });
        } else {
            res.json({ success: false, message: 'No flights found' });
        }
    });
});


//bay


// Route để lấy danh sách chuyến bay
app.get('/flight', (req, res) => {
    const sql = 'SELECT * FROM flight';

    db.query(sql, (err, results) => {
        if (err) {
            return res.json({ success: false, message: 'Failed to fetch flights', error: err });
        }
        if (results.length > 0) {
            res.json({ success: true, flights: results });
        } else {
            res.json({ success: false, message: 'No flights found' });
        }
    });
});
// Route để thêm chuyến bay mới

app.post('/flights', (req, res) => {
    const {
        flight_code, departure_time, arrival_time,
        departure_location, arrival_location,
        total_first_class_seats, available_first_class_seats, first_class_seat_price,
        total_economy_seats, available_economy_seats, economy_seat_price
    } = req.body;
	console.log(req.body);
    // Kiểm tra các trường bắt buộc
    if (!flight_code || !departure_time || !arrival_time || !departure_location ||
        !arrival_location || !total_first_class_seats || !available_first_class_seats ||
        !first_class_seat_price || !total_economy_seats || !available_economy_seats || !economy_seat_price) {
        return res.status(400).json({ success: false, message: 'Tất cả các trường đều bắt buộc' });
    }

    const sql = `INSERT INTO flight 
    (flight_code, departure_time, arrival_time, departure_location, arrival_location, 
    total_first_class_seats, available_first_class_seats, first_class_seat_price, 
    total_economy_seats, available_economy_seats, economy_seat_price) 
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)`;

    const params = [
        flight_code, departure_time, arrival_time, departure_location, arrival_location,
        total_first_class_seats, available_first_class_seats, first_class_seat_price,
        total_economy_seats, available_economy_seats, economy_seat_price
    ];

    db.query(sql, params, (err, result) => {
        if (err) {
            console.error(err); // In ra thông báo lỗi chi tiết
            return res.status(500).json({ success: false, message: 'Failed to add flight', error: err.message });
        }
        res.json({ success: true, message: 'Flight added successfully' });
    });
});




// Route để sửa thông tin chuyến bay
app.put('/flights/:id', (req, res) => {
    const flightId = req.params.id;
    const {
        flight_code, departure_time, arrival_time,
        departure_location, arrival_location,
        total_first_class_seats, available_first_class_seats, first_class_seat_price,
        total_economy_seats, available_economy_seats, economy_seat_price
    } = req.body;
	console.log(req.body);
    const sql = `UPDATE flight SET 
    flight_code = ?, departure_time = ?, arrival_time = ?, 
    departure_location = ?, arrival_location = ?, 
    total_first_class_seats = ?, available_first_class_seats = ?, first_class_seat_price = ?, 
    total_economy_seats = ?, available_economy_seats = ?, economy_seat_price = ?
    WHERE flight_code = ?`;

    const params = [
        flight_code, departure_time, arrival_time, departure_location, arrival_location,
        total_first_class_seats, available_first_class_seats, first_class_seat_price,
        total_economy_seats, available_economy_seats, economy_seat_price, flightId
    ];

    db.query(sql, params, (err, result) => {
        if (err) {
            return res.json({ success: false, message: 'Failed to update flight', error: err });
        }
        res.json({ success: true, message: 'Flight updated successfully' });
    });
});


// Route để xóa chuyến bay
app.delete('/flights/:id', (req, res) => {
    const flightId = req.params.id;

    const sql = 'DELETE FROM flight WHERE flight_code = ?';

    db.query(sql, [flightId], (err, result) => {
        if (err) {
            return res.json({ success: false, message: 'Failed to delete flight', error: err });
        }
        res.json({ success: true, message: 'Flight deleted successfully' });
    });
});

//booking

// Route to handle booking information request
app.get('/bookings', (req, res) => {
    const query = `
        SELECT 
            u.fullname AS booked_by,
            booking_date,
            SUM(CASE 
                WHEN b.seat_type = 0 THEN b.number_of_ticket * f.first_class_seat_price
                WHEN b.seat_type = 1 THEN b.number_of_ticket * f.economy_seat_price
                ELSE 0
            END) AS total_amount
        FROM 
            booking b
        JOIN 
            flight f ON b.flight_code = f.flight_code
        JOIN
            user u ON b.iduser = u.iduser
        GROUP BY 
            u.fullname,
            b.booking_date
    `;

    db.query(query, (err, result) => {
        if (err) {
            res.status(500).json({ success: false, message: 'Error fetching bookings', error: err });
        } else {
            res.json({ success: true, bookings: result });
        }
    });
});

// API để thêm booking
app.post('/bookings', (req, res) => {
  const { iduser, flight_code, seat_type, number_of_ticket } = req.body;

  // Kiểm tra dữ liệu đầu vào
  if (!iduser || !flight_code || seat_type === undefined || !number_of_ticket) {
    return res.status(400).json({ message: 'Thiếu thông tin yêu cầu' });
  }

  // Truy vấn SQL để thêm booking
  const sql = `INSERT INTO booking (iduser, flight_code, seat_type, number_of_ticket) 
               VALUES (?, ?, ?, ?)`;
  const values = [iduser, flight_code, seat_type, number_of_ticket];
  console.log(req.body);
  db.query(sql, values, (err, result) => {
    if (err) {
      console.error('Lỗi khi thêm booking:', err);
      return res.status(500).json({ message: 'Đã xảy ra lỗi khi thêm booking' });
    }

    res.status(201).json({ 
      message: 'Đặt vé thành công', 
      bookingId: result.insertId 
    });
  });
});



app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
});
