import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import PostList from './pages/PostList';
import PostEdit from './pages/PostEdit';
import PostDetail from './pages/PostDetail';
import PrivateRoute from './components/PrivateRoute';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        
        {/* Protected Routes */}
        <Route path="/" element={
          <PrivateRoute>
            <PostList />
          </PrivateRoute>
        } />
        <Route path="/post/create" element={
          <PrivateRoute>
            <PostEdit />
          </PrivateRoute>
        } />
        <Route path="/post/edit/:id" element={
          <PrivateRoute>
            <PostEdit />
          </PrivateRoute>
        } />
        <Route path="/post/:id" element={
          <PrivateRoute>
            <PostDetail />
          </PrivateRoute>
        } />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
