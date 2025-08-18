(module
  (func (export "add") (param f32 f32) (result f32)
    local.get 0
    local.get 1
    f32.add
  )
  (func (export "subtract") (param f32 f32) (result f32)
    local.get 0
    local.get 1
    f32.sub
  )
  (func (export "multiply") (param f32 f32) (result f32)
    local.get 0
    local.get 1
    f32.mul
  )
  (func (export "divide") (param f32 f32) (result f32)
    local.get 0
    local.get 1
    f32.div
  )
)
