import { makeRequest } from 'core/utils/request';
import React, { useState } from 'react';
import BaseForm from '../../BaseForm';
import './styles.scss';

type FormState ={
  name: string;
  price: string;
  category:string;
  description:string;
}

type FormEvent =  React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>;

const Form = () => {
  const [formData, setFormData] = useState<FormState>({
    name: '',
    price: '',
    category: '',
    description: ''
  });
  
  const handleOnChange = (event: FormEvent) =>{
    const name = event.target.name;
    const value = event.target.value;

    setFormData(data => ({ ...data, [name]: value }));
  }
 
  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) =>{
    event.preventDefault();
    const payload = {
      ...formData,
      imgUrl: 'https://compass-ssl.xbox.com/assets/83/53/83534a33-0998-43dc-915a-4ec0a686d679.jpg?n=10202018_Panes-3-up-1400_Hero-SX_570x570.jpg',
      categories:[{id: formData.category}]
    }
    
    makeRequest({url: '/products', method:'POST', data:payload})
      .then(() => {
        setFormData({name:'', category:'', price:'', description:''})
      });

  }

    return(
      <form onSubmit={handleSubmit}>
        <BaseForm  title="cadastrar um produto"> 
           <div className="row">
              <div className="col-6">
                  <input 
                    name="name"
                    type="text" 
                    className="form-control mb-5"
                    onChange={handleOnChange}
                    placeholder="Nome do produto"
                  />
                  <select 
                    className="form-control mb-5" onChange={handleOnChange}
                    name="category"
                  >
                    <option value="1">Livros</option>
                    <option value="3">Computadores</option>
                    <option value="2">Eletrônicos</option>
                  </select>
                  <input 
                    name="price"
                    type="text" 
                    className="form-control"
                    onChange={handleOnChange}
                    placeholder="Preço"
                  />
              </div>
                <div className="col-6">
                  <textarea 
                  name="description"
                  value={formData.description}
                  onChange={handleOnChange}
                  className="form-control"
                  cols={30}
                  rows={10}
                  />
                </div>
          </div>
        </BaseForm>
      </form>
    )
}

export default Form;