-- 1. Tablas independientes (Padres)
-- SUCURSALES (Tierra, Mar y Aire)
INSERT INTO sucursal (id, nombre, direccion, telefono, ciudad)
VALUES (1, 'A todo gas', 'Calle Gran Vía 12', '910000001', 'Madrid');

INSERT INTO sucursal (id, nombre, direccion, telefono, ciudad)
VALUES (2, 'La Costa de Bolivia', 'Paseo Marítimo 45', '930000002', 'Barcelona');

INSERT INTO sucursal (id, nombre, direccion, telefono, ciudad)
VALUES (3, 'Aeropuerto Aterriza Como Puedas', 'Pista 2, Cuidado con los pájaros', '910000003', 'Madrid');

INSERT INTO sucursal (id, nombre, direccion, telefono, ciudad)
VALUES (4, 'Fondo de Bikini', 'Muelle de Poniente 3', '960000004', 'Valencia');

INSERT INTO sucursal (id, nombre, direccion, telefono, ciudad)
VALUES (5, 'Heliopuerto Esperanza', 'Zona Fórum s/n', '930000005', 'Barcelona');

-- CLIENTES
INSERT INTO clientes (id, nombre, apellidos, email)
VALUES (1, 'Ana', 'García López', 'ana.garcia@email.com');

INSERT INTO clientes (id, nombre, apellidos, email)
VALUES (2, 'Elena', 'Nito Del Bosque', 'elena.nito@email.com');

INSERT INTO clientes (id, nombre, apellidos, email)
VALUES (3, 'Carlos', 'Martínez Ruiz', 'carlos.m@email.com');

INSERT INTO clientes (id, nombre, apellidos, email)
VALUES (4, 'Armando', 'Bronca Segura', 'armando.lios@email.com');

INSERT INTO clientes (id, nombre, apellidos, email)
VALUES (5, 'Laura', 'Gómez Díaz', 'laura.gomez@email.com');

INSERT INTO clientes (id, nombre, apellidos, email)
VALUES (6, 'Pere', 'Gil Fresco', 'pere.gil@email.com');

-- 2. Tabla de Vehículos (Depende de Sucursal)
-- VEHÍCULOS DE TIERRA (Sucursales 1 y 2)
INSERT INTO vehiculos (id, matricula, plazas, precio, tipo_vehiculo, sucursal_id)
VALUES (1, '1234ABC', 5, 45.50, 'Turismo', 1);

INSERT INTO vehiculos (id, matricula, plazas, precio, tipo_vehiculo, sucursal_id)
VALUES (2, '5678DEF', 9, 80.00, 'Furgoneta', 1);

INSERT INTO vehiculos (id, matricula, plazas, precio, tipo_vehiculo, sucursal_id)
VALUES (3, '9012GHI', 5, 55.00, 'Turismo', 2);

INSERT INTO vehiculos (id, matricula, plazas, precio, tipo_vehiculo, sucursal_id)
VALUES (4, '3456JKL', 2, 35.00, 'Motocicleta', 2);

-- VEHÍCULOS DE MAR (Sucursal 4 - Puerto)
INSERT INTO vehiculos (id, matricula, plazas, precio, tipo_vehiculo, sucursal_id)
VALUES (5, '7ª-VA-1-23', 2, 150.00, 'Moto de Agua', 4);

INSERT INTO vehiculos (id, matricula, plazas, precio, tipo_vehiculo, sucursal_id)
VALUES (6, '6ª-VA-2-23', 8, 950.00, 'Yate', 4);

-- VEHÍCULOS DE AIRE (Sucursales 3 y 5 - Aeropuerto / Heliopuerto)
INSERT INTO vehiculos (id, matricula, plazas, precio, tipo_vehiculo, sucursal_id)
VALUES (7, 'EC-ABC', 4, 1200.00, 'Avioneta', 3);

INSERT INTO vehiculos (id, matricula, plazas, precio, tipo_vehiculo, sucursal_id)
VALUES (8, 'EC-XYZ', 6, 2500.00, 'Helicóptero', 5);


-- 3. Tabla de Alquileres (Depende de Clientes y Sucursal)
INSERT INTO alquileres (id, cliente_id, sucursal_id, precio_total, fecha_inicio, fecha_fin, estado)
VALUES (1, 1, 1, 136.50, '2024-03-15 10:00:00', '2024-03-18 10:00:00', 'COMPLETADO');

INSERT INTO alquileres (id, cliente_id, sucursal_id, precio_total, fecha_inicio, fecha_fin, estado)
VALUES (2, 2, 2, 55.00, '2024-04-01 09:00:00', '2024-04-02 09:00:00', 'ACTIVO');

INSERT INTO alquileres (id, cliente_id, sucursal_id, precio_total, fecha_inicio, fecha_fin, estado)
VALUES (3, 3, 4, 950.00, '2024-05-10 08:00:00', '2024-05-11 20:00:00', 'RESERVADO'); -- Alquila el Yate


-- 4. Tabla Intermedia N:M (Depende de Alquileres y Vehículos)
-- El alquiler 1 (Ana en Madrid Centro) se lleva el Turismo y la Furgoneta
INSERT INTO alquiler_vehiculos (alquiler_id, vehiculo_id) VALUES (1, 1);
INSERT INTO alquiler_vehiculos (alquiler_id, vehiculo_id) VALUES (1, 2);

-- El alquiler 2 (Carlos en BCN Costa) se lleva el Turismo
INSERT INTO alquiler_vehiculos (alquiler_id, vehiculo_id) VALUES (2, 3);

-- El alquiler 3 (Laura en Puerto Valencia) se lleva el Yate
INSERT INTO alquiler_vehiculos (alquiler_id, vehiculo_id) VALUES (3, 6);